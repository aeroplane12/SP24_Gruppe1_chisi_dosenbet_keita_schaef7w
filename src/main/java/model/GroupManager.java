package model;

import model.tools.Rankable;

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
    private Location partyLoc;
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value == 0 || y.getFoodPref().value == 0? 0 : (x.getFoodPref().value - y.getFoodPref().value)) * Manager.getInstance().getFoodPrefWeight()
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * Manager.getInstance().getAVGAgeRangeWeight()
                + Math.abs(x.getCurrentKitchen().distance(y.getCurrentKitchen())-Manager.getInstance().getOptimalDistance()) * Manager.getInstance().getDistanceWeight()
                - (getGenderDiversity(x.getGenderDiv(),y.getGenderDiv())) * Manager.getInstance().getAVGGenderDIVWeight());
    public GroupManager(){
        // needs to be set for it to work unless the party happens at Null island
        this.partyLoc = new Location(0d,0d);
        instance = this;
    }
    public GroupManager(Location partyLoc){
        this.partyLoc = partyLoc;
        instance = this;
    }
    // copy constructor
    public GroupManager(GroupManager groupManager){
        processedCouples = new ArrayList<>(groupManager.processedCouples);
        overBookedCouples = new ArrayList<>(groupManager.overBookedCouples);
        succeedingCouples = new ArrayList<>(groupManager.succeedingCouples);
        ledger = new ArrayList<>(groupManager.ledger);
        kitchenLedger = new HashMap<>(kitchenLedger);
        partyLoc = groupManager.partyLoc;
    }
    public static GroupManager getInstance() {
        if(instance == null)
            instance = new GroupManager();

        return instance;
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
                        .distance(partyLoc)
                        .compareTo(y.getCurrentKitchen()
                                .distance(partyLoc));
        });
        //O(n*log(n)+n)=O(n*log(n))
        while (possibleHosts.size()%9 != 0 ||
                (possibleHosts.size() > (Manager.maxGuests/2) && !forced) ) {
            // removes the most conflicting and furthest away kitchen Couples first
            // until it reaches a point at which there are possible solutions
            succeedingCouples.add(possibleHosts.remove(0));
            kitchenLedger.get(succeedingCouples.get(succeedingCouples.size()-1).getCurrentKitchen())
                    .remove(succeedingCouples.get(succeedingCouples.size()-1));
        }
        if (possibleHosts.size()==9) {
            succeedingCouples.addAll(possibleHosts);
            for (Couple couple :succeedingCouples) {
                kitchenLedger.get(couple.getCurrentKitchen())
                        .remove(couple);
            }

            return;
        }
        //sort all couples from closest to furthest away from party-Location
        possibleHosts.sort((x,y) ->
                x.getCurrentKitchen()
                .distance(partyLoc)
                .compareTo(y.getCurrentKitchen()
                        .distance(partyLoc)));
        //O(n*log(n)+n*log(n))=O(n*log(n))
        processedCouples.addAll(possibleHosts);
        //O(n*log(n)+n) = O(n*log(n))
        // need everyone from conflicting kitchen to host first, then sort by distance
        for (Course course : new Course[]{Course.DESSERT,Course.DINNER,Course.STARTER}) {
            ledger.addAll(fillCourse(new ArrayList<>(possibleHosts),course));
            //O(n^3))
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
     * fillCourse
     * assigns everyone a group
     * @param course time at which the group gets together
     * @param participants everyone who participates
     * @return the Groups associated with the course
     */
    public List<Group> fillCourse(List<Couple> participants, Course course){
        List<CouplePair> couplePairList = new ArrayList<>();
        int stepSize = participants.size()/3;
        List<Couple> determinedHosts = participants.stream()
                .peek(x->{
                    // if x wasn't host and the kitchen is free, set x as the KitchenUser
                    if (!x.wasHost() &&
                            x.getCurrentKitchen().checkUser(course,-1)){
                        x.getCurrentKitchen().setUser(course,x.getID());
                        x.setHosts(course);
                        //kitchenUserMap.put(x.getCurrentKitchen(), x.getID());
                    }
                })//O(1+n/3) = O(n)
                .filter(x -> !x.wasHost() &&
                                x.getCurrentKitchen().checkUser(course,x.getID())
                        // filter all those who aren't kitchenUsers
                )//O(n/3+n) = O(n)
                .limit(stepSize)
                .toList();
        //O(n)
        List<Couple> guests = new ArrayList<>(participants);
        guests.removeAll(determinedHosts);//O(n^2)
        //O(n+n^2) = O(n^2)
        for (Couple host : determinedHosts) {//O(n*(...))
            Couple firstGuest = guests.stream()
                    .filter(x -> !x.checkMetCouple(host))//O(n)
                    .min((x,y) -> COUPLERANKGEN.rank(host,x).compareTo(COUPLERANKGEN.rank(host,y)))//O(n)
                    .orElseThrow();
            guests.remove(firstGuest);//O(n)
            couplePairList.add(new CouplePair(host,firstGuest));//O(1)
        }
        //O(n^2+n*(n+n+n+1))=O(n^2)

        return findValidCouplePairMatching(//O(n^2+n^3) = O(n^3)
                couplePairList,
                guests,
                course);
    }

    /**
     * findValidCouplePairMatching
     * iterates through each CouplePair Combo and assigns
     * to the least matchable pair their best possible match
     * @param hostGuestCombo tbe hosting Couple and their best guest possible
     * @param allRemainingCouples everyone who isn't already included in hostGuestCombo
     * @param course the time
     * @return all groups for the specified time
     */
    List<Group> findValidCouplePairMatching(List<CouplePair> hostGuestCombo, List<Couple> allRemainingCouples, Course course){
        //runtime Complexity of findValidCouplePairMatching,
        // given hostGuestCombo and allRemainingCouples both have the same size of n/3,
        // therefore we'll define n/3 as m
        List<Group> groups = new ArrayList<>();
        if (allRemainingCouples.isEmpty()) { //O(1)
            return groups;
        }
        Map<CouplePair,List<Couple>> hostMatchMap = new HashMap<>(hostGuestCombo.stream()
                .collect(Collectors
                        .toMap(Function.identity(),//O(m*(...))
                                // Host-Couple pairs mapped to the lowest ranking remaining Couples
                                x -> new ArrayList<>(allRemainingCouples.stream()
                                        .filter(y->!x.history.contains(y))// O(m)
                                        .sorted((a,b)->COUPLERANKGEN.rank(x.host,a).compareTo(COUPLERANKGEN.rank(x.host,b))) //O(m*log(m))
                                        .toList()))));
        //O(m*(m+m*log(m))) = O(m^2*log(m))
        while (!allRemainingCouples.isEmpty()){ // O((m^2*log(m))+m*(...))
            Map.Entry<CouplePair,List<Couple>> leastMatchablePair = hostMatchMap.entrySet()
                    .stream()
                    .min(Comparator.comparingInt(x -> x.getValue().size()))//O(m)
                    .orElseThrow();
            //O(m)
            Couple guest2 = leastMatchablePair.getValue().stream().findFirst().orElseThrow();
            Group group = new Group(leastMatchablePair.getKey().host,
                    leastMatchablePair.getKey().guest,
                    guest2,
                    course,
                    Manager.getGroupCounter());
            //toggles all necessary flags to indicate, that the couples met and the host hosted
            toggleGroupFlags(leastMatchablePair.getKey().host, // O(1)
                    leastMatchablePair.getKey().guest,
                    guest2,
                    course,
                    group.getID());
            //O(1+m)
            //add the group to the output, remove the guest and the pair
            // and remove every instance of the guests in the map values
            hostMatchMap.remove(leastMatchablePair.getKey());
            for (List<Couple> x:hostMatchMap.values()) {//O(m)
                x.remove(guest2);//O(m)
            }
            //O(1+m+m^2))
            groups.add(group);//O(1)
            hostGuestCombo.remove(leastMatchablePair.getKey());//O(m)
            allRemainingCouples.remove(guest2);//O(m)
            //O(1+m+m^2)
        }
        // O(m+m^2+m^2*log(m)+m^3) = O(m^3)
        return groups;

    }


    public List<Group> getLedger() {
        return ledger;
    }
    public static void clear(){
        /*
        List<Couple> retry = new ArrayList<>(getInstance().processedCouples);
        retry.addAll(getInstance().overBookedCouples);
        retry.addAll(getInstance().succeedingCouples);
        retry.forEach(x->{
            x.getCurrentKitchen().clearFlags();
            x.clearFlags();}
        );
         */
        getInstance().processedCouples.clear();
        getInstance().succeedingCouples.clear();
        getInstance().overBookedCouples.clear();
        getInstance().kitchenLedger.clear();
        getInstance().ledger.clear();
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

    /**
     * inner-class for grouping a host and a guest together
     */
    private class CouplePair{
        Couple host;
        Couple guest;
        Set<Couple> history;
        CouplePair(Couple host, Couple g1){
            this.host = host;
            this.guest = g1;
            history = new HashSet<>(host.getMetCouples());
            history.addAll(g1.getMetCouples());
        }
    }
    public void setPartyLoc(Location partyLoc) {
        this.partyLoc = partyLoc;
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
        if (pendingGroup.isEmpty()||!processedCouples.contains(couple)) {
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
}
