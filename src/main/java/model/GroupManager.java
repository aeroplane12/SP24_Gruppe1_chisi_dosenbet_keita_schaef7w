package model;

import model.tools.Rankable;

import java.util.*;

public class GroupManager {
    private Double FOODPREFWEIGHT;
    private Double AVGAGERANGEWEIGHT;
    private Double AVGGENDERDIVWEIGHT;
    public List<Couple> allCouples;
    private List<Couple> succeedingCouples = new ArrayList<>();
    public List<Couple> overBookedCouples = new ArrayList<>(); //couples that are sadly not usable unless a conflicting couple gets deleted

    private static GroupManager instance;
    public Map<Course,List<Group>> ledger = new HashMap<>(Map.of(
            Course.DESSERT,new ArrayList<>(),
            Course.DINNER,new ArrayList<>(),
            Course.STARTER,new ArrayList<>()));
    private Map<Kitchen,List<Couple>> kitchenLedger;
    public Location partyLoc;
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value == 0 || y.getFoodPref().value == 0?
                0 : (x.getFoodPref().value - y.getFoodPref().value)) * FOODPREFWEIGHT
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * AVGAGERANGEWEIGHT
                - Math.abs(x.getGenderAVG() - y.getGenderAVG()) * AVGGENDERDIVWEIGHT);
    public GroupManager(){
        FOODPREFWEIGHT = 0d;
        AVGAGERANGEWEIGHT = 0d;
        AVGGENDERDIVWEIGHT = 0d;
        instance = this;
    }

    public GroupManager(Double foodPrefWeight,Double avgAgeRangeWeight,Double avgGenderDiffWeight){
        FOODPREFWEIGHT = foodPrefWeight;
        AVGAGERANGEWEIGHT = avgAgeRangeWeight;
        AVGGENDERDIVWEIGHT = avgGenderDiffWeight;
        instance = this;
    }

    public static GroupManager getInstance() {
        if(instance == null)
            instance = new GroupManager();

        return instance;
    }


    public void calcGroups(List<Couple> allCouples){
        /*TODO sorting couples into Groups according to specifications
         - each group consists of three couples, one host and two guests
         - create groups according to the following specifications
         - first by food preference, meaties with meaties and nones ,veggies with vegans and other veggies
         - second by location, small distances but closing in on partyLoc
         - third by age, keeping the ageRange minimal
         - finally by gender, keeping genders in each group as diverse as possible
         - AVOID USING A KITCHEN MORE THAN THREE TIMES
         */
        this.allCouples = allCouples;
        resolveKitchenConflicts();
        List<Couple> possibleHosts = this.allCouples;
        possibleHosts.sort( (x,y) ->{
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
        while (possibleHosts.size()%9 != 0) {
            // removes the most conflicting, furthest kitchen
            // until it reaches a point at which there are possible solutions
            succeedingCouples.add(possibleHosts.remove(0));
        }
        possibleHosts.sort( (x,y) ->{
            int z = Integer.compare(
                    kitchenLedger.get(x.getCurrentKitchen()).size(),
                    kitchenLedger.get(y.getCurrentKitchen()).size());
            if (z != 0) {
                return -1*z;
            }
            return x.getCurrentKitchen()
                    .distance(partyLoc)
                    .compareTo(y.getCurrentKitchen()
                            .distance(partyLoc));
        });
        // need everyone from conflicting kitchen to host first, then sort by distance
        for (Course course : new Course[]{Course.DESSERT,Course.DINNER,Course.STARTER}) {
            ledger.put(course,fillCourse(new ArrayList<>(possibleHosts),course));
        }

    }

    /**
     * kitchenConflicts
     * @param kitchen determines whether this kitchen is even assignable
     *                or if there are too many couples designating
     *                this as their primary kitchen
     * @return all conflicting Couples
     */
    public List<Couple> kitchenConflicts(Kitchen kitchen){
        List<Couple> output = new ArrayList<>();
        for (Couple i : allCouples) {
            if (i.getCurrentKitchen() == kitchen) {
                output.add(i);
            }
        }
        return output;
    }

    /**
     * resolvesKitchenConflicts
     * detects and resolves KitchenConflicts
     * by either switching the couple-kitchen or moving the couple to the succeedingCouples list
     */
    public void resolveKitchenConflicts(){
        // first mapping all  kitchen to their owners
        Map<Kitchen,List<Couple>> kitchenAmountLedger = new HashMap<>();
        for (Couple i : allCouples){
            if (!kitchenAmountLedger.containsKey(i.getCurrentKitchen())) {
                kitchenAmountLedger.put(i.getCurrentKitchen(),kitchenConflicts(i.getCurrentKitchen()));
            }
        }
        // removing all kitchen with less than three owners
        kitchenLedger = new HashMap<>(kitchenAmountLedger);//copying into a central ledger
        kitchenAmountLedger.values().removeIf(x->x.size()<3);
        for (List<Couple> couples : kitchenAmountLedger.values()){
            int neededRep = couples.size();
            for (Couple c : couples) {
                if (c.getOtherKitchen()!=null &&
                        !kitchenAmountLedger.containsKey(c.getOtherKitchen())&&
                        neededRep > 3) {
                    //if the partner has another kitchen, and it's not also overbooked, use it
                    kitchenLedger.get(c.getCurrentKitchen()).remove(c);
                    c.toggleWhoseKitchen();
                    neededRep--;
                }
            }
            while (neededRep > 3) {
                // removes all conflicting entries until there are no more than three
                allCouples.remove(couples.get(0));
                overBookedCouples.add(couples.get(0));
                kitchenLedger.get(couples.get(0).getOtherKitchen()).remove(couples.get(0));
                neededRep--;
            }
        }

    }
    /**
     * fillCourse
     * fills a course
     * @param course
     * @param hosts all possible hosts
     * @return the Groups associated with the course
     */
    public List<Group> fillCourse(List<Couple> hosts, Course course){
        List<Group> assortedGroups = new ArrayList<>();
        while (!hosts.isEmpty()) {
            assortedGroups.add(findGuests(hosts.stream().filter(x->!x.WasHost() &&
                    x.getCurrentKitchen().checkUse(course)).findFirst().orElseThrow(),hosts,course));
            hosts.removeAll(assortedGroups.get(assortedGroups.size()-1).getAll());
        }
        return assortedGroups;
    }

    public Group findGuests(Couple host, List<Couple> possibleMatches, Course course){
        List<Couple> potentialGuests = possibleMatches.stream()
                .filter(x->!x.getMetCouple().contains(host) ||
                        !x.equals(host))
                .sorted((x,y)-> COUPLERANKGEN.rank(host,x).compareTo(COUPLERANKGEN.rank(host,y)))
                .limit(2)
                .toList();
        Couple g1 = potentialGuests.get(0);
        Couple g2 = potentialGuests.get(1);
        Group group = new Group(host,g1,g2,course,Manager.getGroupCounter());
        host.meetsCouple(g1);
        host.meetsCouple(g2);
        host.getWithWhomAmIEating().put(course,group.getID());
        host.getCurrentKitchen().setUse(course);
        host.toggleWasHost();
        g1.meetsCouple(g2);
        g1.meetsCouple(host);
        g1.getWithWhomAmIEating().put(course,group.getID());
        g2.meetsCouple(g1);
        g2.meetsCouple(host);
        g2.getWithWhomAmIEating().put(course,group.getID());
        possibleMatches.removeAll(List.of(host,g1,g2));

        //not very efficient but hopefully functional
        return group;
    }

    public Double getFOODPREFWEIGHT() {
        return FOODPREFWEIGHT;
    }

    public void setFOODPREFWEIGHT(Double FOODPREFWEIGHT) {
        this.FOODPREFWEIGHT = FOODPREFWEIGHT;
    }

    public Double getAVGAGERANGEWEIGHT() {
        return AVGAGERANGEWEIGHT;
    }

    public void setAVGAGERANGEWEIGHT(Double AVGAGERANGEWEIGHT) {
        this.AVGAGERANGEWEIGHT = AVGAGERANGEWEIGHT;
    }

    public Double getAVGGENDERDIVWEIGHT() {
        return AVGGENDERDIVWEIGHT;
    }

    public void setAVGGENDERDIVWEIGHT(Double AVGGENDERDIVWEIGHT) {
        this.AVGGENDERDIVWEIGHT = AVGGENDERDIVWEIGHT;
    }

}
