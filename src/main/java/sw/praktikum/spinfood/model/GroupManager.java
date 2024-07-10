package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GroupManager {
    public List<Couple> succeedingCouples = new ArrayList<>();
    public List<Couple> overBookedCouples = new ArrayList<>(); //couples that are sadly not usable unless a conflicting couple gets deleted
    public List<Couple> processedCouples = new ArrayList<>();
    private static GroupManager instance;
    public List<Group> ledger = new ArrayList<>();
    private Map<Kitchen,List<Couple>> kitchenLedger = new HashMap<>();
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value == 0 || y.getFoodPref().value == 0? 0 : (x.getFoodPref().value - y.getFoodPref().value)) * Manager.getInstance().getFoodPrefWeight()
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * Manager.getInstance().getAVGAgeRangeWeight()
                + Math.abs(x.getCurrentKitchen().distance(y.getCurrentKitchen())-Manager.getInstance().getOptimalDistance()) * Manager.getInstance().getDistanceWeight()
                - (getGenderDiversity(x.getGenderDiv(),y.getGenderDiv())) * Manager.getInstance().getAVGGenderDIVWeight());
    public GroupManager(){
        instance = this;
    }
    // copy constructor
    public GroupManager(GroupManager groupManager){
        processedCouples = new ArrayList<>(groupManager.processedCouples);
        overBookedCouples = new ArrayList<>(groupManager.overBookedCouples);
        succeedingCouples = new ArrayList<>(groupManager.succeedingCouples);
        ledger = new ArrayList<>(groupManager.ledger);
        kitchenLedger = new HashMap<>(kitchenLedger);
    }
    public static GroupManager getInstance() {
        if(instance == null)
            instance = new GroupManager();

        return instance;
    }
    public static void setInstance(GroupManager groupManager) {
        instance = groupManager;
    }

    public void calcGroups(List<Couple> allCouples,boolean forced){
        /*DONE sorting couples into Groups according to specifications
         - each group consists of three couples, one host and two guests
         - create groups according to the following specifications
         - first by food preference, meat-lovers with meat-lovers and nones, veggies with vegans and other veggies
         - second by location, small distances but closing in on partyLoc
         - third by age, keeping the ageRange minimal
         - finally by gender, keeping genders in each group as diverse as possible
         - AVOID USING A KITCHEN MORE THAN THREE TIMES
         */
        if (allCouples.isEmpty()) {
            return;
        }
        if (forced) {
            succeedingCouples.removeAll(allCouples);
            overBookedCouples.removeAll(allCouples);
        }
        List<Couple> possibleHosts = resolveKitchenConflicts(new ArrayList<>(allCouples));
        //O(n)
        possibleHosts.sort((x,y) ->{
            int z = Integer.compare(
                    kitchenLedger.get(x.getCurrentKitchen()).size(),
                    kitchenLedger.get(y.getCurrentKitchen()).size());
            if (z != 0) {
                return -1*z;
            }
            return -1*x.getCurrentKitchen()
                    .distance(Manager.getInstance().partyLoc)
                    .compareTo(y.getCurrentKitchen()
                            .distance(Manager.getInstance().partyLoc));
        });
        //O(n*log(n)+n)=O(n*log(n))
        while (possibleHosts.size()%9 != 0 ||
                (possibleHosts.size() > (Manager.maxGuests/2) && !forced) ) {
            Couple coupleForRemoval = possibleHosts.stream()
                    .max((x,y)->Integer.compare(
                    kitchenLedger.get(x.getCurrentKitchen()).size(),
                    kitchenLedger.get(y.getCurrentKitchen()).size())).get();
            // removes the most conflicting and furthest away kitchen Couples first
            // until it reaches a point at which there are possible solutions
            possibleHosts.remove(coupleForRemoval);
            succeedingCouples.add(coupleForRemoval);
            kitchenLedger.get(coupleForRemoval.getCurrentKitchen())
                    .remove(coupleForRemoval);
        }
        processedCouples.addAll(possibleHosts);

        // divide matching 9 couples into 9 groups (representing the smallest possible group-cluster),
        // - who is hosting must be matched with kitchen availability, sorted by distance from partyloc
        // -
        List<Couple[]> allMatches = matchNineCouples(new ArrayList<>(processedCouples));
        for (Couple[] i :allMatches) {
            ledger.addAll(createCluster(i));
        }
    }

    /**
     * resolvesKitchenConflicts
     * detects and resolves KitchenConflicts
     * by either switching the couple-kitchen or moving the couple to the succeedingCouples list
     */
    public List<Couple> resolveKitchenConflicts(List<Couple> allCouples){
        // first mapping all kitchen to their owners
        List<Couple> output = new ArrayList<>(allCouples);//O(n)
        int maxKitchenUsage = 3;
        for (Couple couple : allCouples) {//O(n)
            Kitchen currKitchen = couple.getCurrentKitchen();

            if (!kitchenLedger.containsKey(currKitchen)) {
                //if there are no kitchen like this in the register
                kitchenLedger.put(currKitchen,new ArrayList<>(List.of(couple)));
            } else if (kitchenLedger.get(currKitchen).size() < maxKitchenUsage){
                // if there are less than or exactly max kitchen occurrences present
                kitchenLedger.get(currKitchen).add(couple);
            } else {
                if (couple.getOtherKitchen()!=null) {
                    //if there is another kitchen
                    if (!kitchenLedger.containsKey(couple.getOtherKitchen())) {
                        // and it's not yet in the registry
                        couple.toggleWhoseKitchen();
                        kitchenLedger.put(couple.getCurrentKitchen(),new ArrayList<>(List.of(couple)));
                    } else if (kitchenLedger.get(couple.getOtherKitchen()).size() < maxKitchenUsage){
                        // or there are less than max kitchen in this registry
                        couple.toggleWhoseKitchen();
                        kitchenLedger.get(couple.getCurrentKitchen()).add(couple);
                    } else {
                        // its already present three times
                        overBookedCouples.add(couple);
                        output.remove(couple);
                    }
                } else {
                    // if there are more than 3 of this kitchen already in the ledger remove
                    // all further couples owning this kitchen
                    overBookedCouples.add(couple);
                    output.remove(couple);
                }
            }
        }
        return output;
        //O(n)
    }

    /**
     * matchNineCouples
     * @param allCouples all Couples
     * @return nine Couples, matched according to specs and each array seperated into:
     *      - the furthest 3, hosting dessert
     *      - the middle 3, hosting dinner
     *      - the closest 3, hosting starter
     *      in regard to the partyLocation
     */
    private List<Couple[]> matchNineCouples(List<Couple> allCouples){
        allCouples.sort((x,y) ->{
            int z = Integer.compare(
                    kitchenLedger.get(x.getCurrentKitchen()).size(),
                    kitchenLedger.get(y.getCurrentKitchen()).size());
            if (z != 0) {
                return -1*z;
            }
            return -1*x.getCurrentKitchen()
                    .distance(Manager.getInstance().partyLoc)
                    .compareTo(y.getCurrentKitchen()
                            .distance(Manager.getInstance().partyLoc));
        });
        // sorts from farthest to closest
        List<Couple[]> output = new ArrayList<>();
        // find the three closest Couples and fit them according to the first selection
        int size = allCouples.size()/9;
        for (int j = 0; j<size; j++) {
            Couple[] currCluster = new Couple[9];
            for (Course course : Course.values()) {
                fillLayer(allCouples, currCluster, course);
            }
            output.add(currCluster);
        }

        return output;
    }
    /**
     * fillLayer
     * @param couples all remaining unmatched couples
     * @param cluster current cluster
     * @param course  the time
     */
    private void fillLayer(List<Couple> couples,
                           Couple[] cluster,
                           Course course){
        Couple a;
        if (Arrays.stream(cluster).anyMatch(Objects::nonNull)){
            a = couples.stream()
                    .filter(x->x.getCurrentKitchen().checkUser(course,-1))
                    .findFirst().get();
        } else {
            a = couples.stream()
                    .filter(x->x.getCurrentKitchen().checkUser(course,-1))
                    .min((x,y)->{
                        int out = 0;
                        for (Couple couple : cluster) {
                            if (couple!=null) {
                                out += COUPLERANKGEN.rank(couple,x).compareTo(COUPLERANKGEN.rank(couple,y));
                            }
                        }
                        return out;})
                    .get();

        }
        a.getCurrentKitchen().setUser(course,a.getID());
        couples.remove(a);
        cluster[3 * (course.value-1)] = a;

        Couple b = couples.stream()
                .filter(x->x.getCurrentKitchen().checkUser(course,-1))
                .min((x,y)->{
                    int out = 0;
                    for (Couple couple : cluster) {
                        if (couple!=null) {
                            out += COUPLERANKGEN.rank(couple,x).compareTo(COUPLERANKGEN.rank(couple,y));
                        }
                    }
                    return out;})
                .get();

        b.getCurrentKitchen().setUser(course,b.getID());
        couples.remove(b);
        cluster[1 + 3 * (course.value-1)] = b;
        Couple c = couples.stream()
                .filter(x->x.getCurrentKitchen().checkUser(course,-1))
                .min((x,y)->{
                    int out = 0;
                    for (Couple couple : cluster) {
                        if (couple!=null) {
                            out += COUPLERANKGEN.rank(couple,x).compareTo(COUPLERANKGEN.rank(couple,y));
                        }
                    }
                    return out;})
                .get();
        c.getCurrentKitchen().setUser(course,c.getID());
        couples.remove(c);
        cluster[2 + 3 * (course.value-1)] = c;
    }

    /**
     * createCluster
     * @param couples nine Couples, matched according to specs and each array seperated into:
     *      - the furthest 3, hosting dessert
     *      - the middle 3, hosting dinner
     *      - the closest 3, hosting starter
     *      in regard to the partyLocation
     * @return the 9 matching Groups
     */
    private List<Group> createCluster(Couple[] couples){
                //last hosts
        Couple  a = couples[0],
                d = couples[1],
                g = couples[2],
                // middle hosts
                b = couples[3],
                e = couples[4],
                h = couples[5],
                // first hosts
                c = couples[6],
                f = couples[7],
                i = couples[8];
        List<Group> output = new ArrayList<>();
        //dessert
        output.add(new Group(a,b,c,Course.DESSERT,Manager.getGroupCounter()));
        output.add(new Group(d,e,f,Course.DESSERT,Manager.getGroupCounter()));
        output.add(new Group(g,h,i,Course.DESSERT,Manager.getGroupCounter()));
        toggleGroupFlags(a,b,c,Course.DESSERT,output.get(0).getID());
        toggleGroupFlags(d,e,f,Course.DESSERT,output.get(output.size()-2).getID());
        toggleGroupFlags(g,h,i,Course.DESSERT,output.get(output.size()-1).getID());
        //dinner
        output.add(new Group(b,d,i,Course.DINNER,Manager.getGroupCounter()));
        output.add(new Group(e,c,g,Course.DINNER,Manager.getGroupCounter()));
        output.add(new Group(h,f,a,Course.DINNER,Manager.getGroupCounter()));
        toggleGroupFlags(b,d,i,Course.DINNER,output.get(output.size()-3).getID());
        toggleGroupFlags(e,c,g,Course.DINNER,output.get(output.size()-2).getID());
        toggleGroupFlags(h,f,a,Course.DINNER,output.get(output.size()-1).getID());
        //starter
        output.add(new Group(c,d,h,Course.STARTER,Manager.getGroupCounter()));
        output.add(new Group(f,g,b,Course.STARTER,Manager.getGroupCounter()));
        output.add(new Group(i,e,a,Course.STARTER,Manager.getGroupCounter()));
        toggleGroupFlags(c,d,h,Course.STARTER,output.get(output.size()-3).getID());
        toggleGroupFlags(f,g,b,Course.STARTER,output.get(output.size()-2).getID());
        toggleGroupFlags(i,e,a,Course.STARTER,output.get(output.size()-1).getID());
        return output;
    }


    public List<Group> getLedger() {
        return ledger;
    }
    public void clear(){
        List<Couple> retry = new ArrayList<>(getInstance().processedCouples);
        retry.addAll(getInstance().overBookedCouples);
        retry.addAll(getInstance().succeedingCouples);
        retry.forEach(x->{
            x.getCurrentKitchen().clearFlags();
            x.clearFlags();}
        );
        processedCouples.clear();
        succeedingCouples.clear();
        overBookedCouples.clear();
        kitchenLedger.clear();
        ledger.clear();
    }

    /**
     * toggles all the needed GroupFlags in both kitchen and Couple
     * @param groupID the groupID
     * @param host the host
     * @param guest1 the first guest
     * @param guest2 the second guest
     * @param course the time
     */
    private void toggleGroupFlags(Couple host, Couple guest1, Couple guest2, Course course, int groupID){
        host.meetsCouple(guest1);
        host.meetsCouple(guest2);
        host.setHosts(course);
        host.isHost();
        host.putWithWhomAmIEating(course,groupID);
        guest1.meetsCouple(guest2);
        guest1.meetsCouple(host);
        guest1.putWithWhomAmIEating(course,groupID);
        guest2.meetsCouple(guest1);
        guest2.meetsCouple(host);
        guest2.putWithWhomAmIEating(course,groupID);
    }

    /**
     * getGenderDiversity
     * @param x genderSet of the first Couple
     * @param y genderSet of the second Couple
     * @return the size of the set divided by the amount of possibilities, in this case 3, meaning values range from 1/3 to 1
     */
    Double getGenderDiversity(Set<Gender.genderValue> x, Set<Gender.genderValue> y){
        x.addAll(y);
        return x.size()/3d;
    }
    public Map<Kitchen, List<Couple>> getKitchenLedger() {
        return kitchenLedger;
    }
    /**
     * removes Couple from all Groups
     * @param couple the couple-to-be-removed
     */
    public void remove(Couple couple){
        if (couple == null) {
            return;
        }

        List<Group> pendingGroup = ledger.stream()
                .filter(x->x.getAll().contains(couple)).toList();
        if (pendingGroup.isEmpty() || !processedCouples.contains(couple)) {
            // if element is not in ledger, it must already be in succeeding list or overbooked list
            overBookedCouples.remove(couple);
            succeedingCouples.remove(couple);
            return;
        }
        Course coupleCourse = couple.getHosts();
        List<Couple> possibleSuccessors = new ArrayList<>(succeedingCouples);
        // if it is from an overbooked kitchen, get all ppl. who also are from that kitchen and add them as successors
        if (kitchenLedger.get(couple.getCurrentKitchen()).size()==3) {
            possibleSuccessors.addAll(new ArrayList<>(overBookedCouples.stream()
                    .filter(x->x.getCurrentKitchen()
                            .equals(couple.getCurrentKitchen()))
                    .toList()));
        }
        processedCouples.remove(couple);
        //remove from kitchen ledger and the other kitchen registries
        couple.getCurrentKitchen().clearUser(couple);
        kitchenLedger.get(couple.getCurrentKitchen()).remove(couple);
        // choose couple, which has a free kitchen
        Couple replacement = possibleSuccessors.stream()
                .filter(x->kitchenLedger.containsKey(x.getCurrentKitchen())
                        && x.getCurrentKitchen().checkUser(coupleCourse,-1))
                .min((x,y)-> COUPLERANKGEN.rank(couple,x)
                        .compareTo(COUPLERANKGEN.rank(couple,y))).orElse(null);
        if (replacement == null) {
            // if you can't pull from successors just recalculate everything
            List<Couple> retry = new ArrayList<>(processedCouples);
            retry.addAll(overBookedCouples);
            retry.addAll(succeedingCouples);
            retry.forEach(x->{
                x.getCurrentKitchen().clearFlags();
                x.clearFlags();});
            clear();
            calcGroups(retry,false);
            return;
        }
        succeedingCouples.remove(replacement);
        overBookedCouples.remove(replacement);
        processedCouples.add(replacement);
        replacement.setMetCouples(new HashSet<>(couple.getMetCouples().stream()
                .filter(x->!x.equals(couple))
                .collect(Collectors.toSet())));
        replacement.meetsCouple(replacement);
        replacement.isHost();

        replacement.setHosts(coupleCourse);
        replacement.putWithWhomAmIEating(pendingGroup.get(0).course,pendingGroup.get(0).getID());
        replacement.putWithWhomAmIEating(pendingGroup.get(1).course,pendingGroup.get(1).getID());
        replacement.putWithWhomAmIEating(pendingGroup.get(2).course,pendingGroup.get(2).getID());

        List<Group> groupsForRemoval = pendingGroup.stream().toList();
        for (Group group : groupsForRemoval) {
            group.replaceCouple(couple,replacement);
            for (Couple i: group.getAll()) {
                i.getMetCouples().remove(couple);
                i.meetsCouple(replacement);
            }
            replacement.putWithWhomAmIEating(group.course,group.getID());
        }
    }

    /** forceGroups
     * forces the addition of given Couples,
     * regardless of underlying size limitations of PartyLocation
     * @param couples additional couples, need to be at-least 18 :( couples
     */
    public void forceGroups(List<Couple> couples){
        List<Couple> sC = new ArrayList<>(succeedingCouples);
        List<Couple> oBC = new ArrayList<>(overBookedCouples);
        List<Couple> retry = new ArrayList<>(processedCouples);
        retry.addAll(couples);
        retry.forEach(x->{
            x.getCurrentKitchen().clearFlags();
            x.clearFlags();});
        clear();
        succeedingCouples = sC;
        overBookedCouples = oBC;
        calcGroups(retry,true);
    }

    /**
     * calculates the cumulative ranks of every given couple
     * @param a
     * @param b
     * @return
     */
    static public Double[] compareGroupLists(List<Group> a, List<Group> b){
        Double aScore=0d;
        Double bScore=0d;
        Rankable<Couple> ranker = GroupManager.getInstance().COUPLERANKGEN;
        for (Group i : a) {
            Couple host = i.getHosts();
            for (Couple j : i.getAll()){
                aScore += ranker.rank(host,j);
            }
        }
        for (Group i : b) {
            Couple host = i.getHosts();
            for (Couple j : i.getAll()){
                bScore += ranker.rank(host,j);
            }
        }
        return new Double[]{aScore,bScore,aScore - bScore};
    }
}
