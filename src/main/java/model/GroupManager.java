package model;

import model.tools.Rankable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GroupManager {
    private Double FoodPrefWeight;
    private Double AVGAgeRangeWeight;
    private Double AVGGenderDIVWeight;
    public List<Couple> succeedingCouples = new ArrayList<>();
    public List<Couple> overBookedCouples = new ArrayList<>(); //couples that are sadly not usable unless a conflicting couple gets deleted
    private Double distanceWeight;
    private Double optimalDistance;
    private static GroupManager instance;
    private final static List<Group> ledger = new ArrayList<>();
    private Map<Kitchen,List<Couple>> kitchenLedger = new HashMap<>();
    public Location partyLoc;
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value == 0 || y.getFoodPref().value == 0?
                0 : (x.getFoodPref().value - y.getFoodPref().value)) * FoodPrefWeight
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * AVGAgeRangeWeight
                + Math.pow(x.getCurrentKitchen().distance(y.getCurrentKitchen())-optimalDistance,2)* distanceWeight
                - Math.abs(x.getGenderAVG() - y.getGenderAVG()) * AVGGenderDIVWeight);
    public GroupManager(){
        FoodPrefWeight = 1d;
        AVGAgeRangeWeight = 1d;
        AVGGenderDIVWeight = 1d;
        distanceWeight = 1d;
        optimalDistance = 100d;
        this.partyLoc = new Location(0d,0d);
        instance = this;
    }

    public GroupManager(Double foodPrefWeight,
                        Double avgAgeRangeWeight,
                        Double avgGenderDiffWeight,
                        Double optimalDistance,
                        Double distanceWeight,
                        Location partyLoc){
        FoodPrefWeight = foodPrefWeight;
        AVGAgeRangeWeight = avgAgeRangeWeight;
        AVGGenderDIVWeight = avgGenderDiffWeight;
        this.optimalDistance = optimalDistance;
        this.distanceWeight = distanceWeight;
        this.partyLoc = partyLoc;
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
        if (allCouples.isEmpty()) {
            return;
        }
        List<Couple> possibleHosts = resolveKitchenConflicts(new ArrayList<>(allCouples));
        if (possibleHosts.isEmpty()) {
            return;
        }
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
        while (possibleHosts.size()%9 != 0) {
            // moves the most conflicting and furthest away kitchen Couples first
            // until it reaches a point at which there are possible solutions
            succeedingCouples.add(possibleHosts.remove(0));
            kitchenLedger.get(succeedingCouples.get(succeedingCouples.size()-1).getCurrentKitchen())
                    .remove(succeedingCouples.get(succeedingCouples.size()-1));
        }
        possibleHosts.sort((x,y) ->{
            // priority must be reserved for kitchen with high ownership
            int z = -1*Integer.compare(kitchenLedger.get(x.getCurrentKitchen()).size(),kitchenLedger.get(y.getCurrentKitchen()).size());
            if (z != 0) {
                return z;
            }
            return x.getCurrentKitchen()
                    .distance(partyLoc)
                    .compareTo(y.getCurrentKitchen()
                            .distance(partyLoc));
        });
        // need everyone from conflicting kitchen to host first, then sort by distance
        for (Course course : new Course[]{Course.DESSERT,Course.DINNER,Course.STARTER}) {
            ledger.addAll(fillCourse(new ArrayList<>(possibleHosts),course));
        }

    }

    /**
     * resolvesKitchenConflicts
     * detects and resolves KitchenConflicts
     * by either switching the couple-kitchen or moving the couple to the succeedingCouples list
     */
    public List<Couple> resolveKitchenConflicts(List<Couple> allCouples){
        // first mapping all kitchen to their owners
        List<Couple> output = new ArrayList<>(allCouples);
        kitchenLedger = new HashMap<>();
        int maxKitchenUsage = 3;
        for (Couple couple : allCouples) {
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
                    couple.toggleWhoseKitchen();
                    if (!kitchenLedger.containsKey(couple.getCurrentKitchen())) {
                        // and it's not yet in the registry
                         kitchenLedger.put(couple.getCurrentKitchen(),new ArrayList<>(List.of(couple)));
                    } else if (kitchenLedger.get(couple.getCurrentKitchen()).size() < maxKitchenUsage){
                        // or there are less than max kitchen in this registry
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
    }

    /**
     * fillCourse
     * assigns everyone a group
     * @param course time at which the group gets together
     * @param participants everyone who participates
     * @return the Groups associated with the course
     */
    public List<Group> fillCourse(List<Couple> participants, Course course){
        //List<Group> assortedGroups = new ArrayList<>(); // the output
        List<CouplePair> couplePairList = new ArrayList<>();
        int stepSize = participants.size()/3;
        List<Couple> determinedHosts = participants.stream()
                .peek(x->{
                    // if x wasn't host and the kitchen is free, set x as the KitchenUser
                    if (!x.wasHost() &&
                            x.getCurrentKitchen().checkUser(course,-1)){
                        x.getCurrentKitchen().setUser(course,x.getID());
                        //kitchenUserMap.put(x.getCurrentKitchen(), x.getID());
                    }
                })
                .filter(x -> !x.wasHost() &&
                                x.getCurrentKitchen().checkUser(course,x.getID())
                        // filter all those who aren't kitchenUsers
                ).distinct()
                .limit(stepSize)
                .toList();
        List<Couple> guests = new ArrayList<>(participants);
        guests.removeAll(determinedHosts);
        for (Couple host : determinedHosts) {
            Couple firstGuest = guests.stream()
                    .filter(x -> !x.checkMetCouple(host))
                    .min((x,y) -> COUPLERANKGEN.rank(host,x).compareTo(COUPLERANKGEN.rank(host,y)))
                    .orElseThrow();
            guests.remove(firstGuest);
            couplePairList.add(new CouplePair(host,firstGuest));
        }
        //kitchenUsageLedger.put(course,kitchenUserMap);

        return findValidCouplePairMatching(couplePairList,
                guests,
                course,
                new ArrayList<>());
    }


    List<Group> findValidCouplePairMatching(List<CouplePair> hostGuestCombo, List<Couple> allRemainingCouples, Course course,List<Group> groups){
        if (allRemainingCouples.isEmpty()) {
            return groups;
        }
        Map<CouplePair,List<Couple>> hostMatchMap = new HashMap<>(hostGuestCombo.stream()
                .collect(Collectors
                        .toMap(Function.identity(),
                                // Host-Couple pairs mapped to lowest ranking remaining Couples
                                x -> new ArrayList<>(allRemainingCouples.stream()
                                        .filter(y->!x.history.contains(y))
                                        .sorted((a,b)->COUPLERANKGEN.rank(x.host,a).compareTo(COUPLERANKGEN.rank(x.host,b)))
                                        .toList()))));
        boolean changeFlag = false;
        for (Map.Entry<CouplePair, List<Couple>> hostMatches : hostMatchMap.entrySet()) {
            if (hostMatches.getValue().size() == 1) {
                Couple guest2 = hostMatches.getValue().stream().findFirst().get();
                Group group = new Group(hostMatches.getKey().host,
                        hostMatches.getKey().guest,
                        guest2,
                        course,
                        Manager.getGroupCounter());
                // toggle all flags
                toggleGroupFlags(hostMatches.getKey().host,
                        hostMatches.getKey().guest,
                        guest2,
                        course,
                        group.getID());
                groups.add(group);
                hostGuestCombo.remove(hostMatches.getKey());
                allRemainingCouples.remove(guest2);
                return findValidCouplePairMatching(hostGuestCombo, allRemainingCouples, course, groups);
            }
        }
        if (allRemainingCouples.isEmpty()) {
            return groups;
        }
        Map.Entry<CouplePair,List<Couple>> leastMatchablePair = hostMatchMap.entrySet()
                .stream()
                .min(Comparator.comparingInt(x -> x.getValue().size()))
                .orElseThrow();
        Couple guest2 = leastMatchablePair.getValue().stream().findFirst().orElseThrow();
        Group group = new Group(leastMatchablePair.getKey().host,
                leastMatchablePair.getKey().guest,
                guest2,
                course,
                Manager.getGroupCounter());
        toggleGroupFlags(leastMatchablePair.getKey().host,
                leastMatchablePair.getKey().guest,
                guest2,
                course,
                group.getID());
        groups.add(group);
        hostGuestCombo.remove(leastMatchablePair.getKey());
        allRemainingCouples.remove(guest2);

        return findValidCouplePairMatching(hostGuestCombo,
                allRemainingCouples,
                course,
                groups);
    }

    public Double getFoodPrefWeight() {
        return FoodPrefWeight;
    }

    public void setFoodPrefWeight(Double foodPrefWeight) {
        this.FoodPrefWeight = foodPrefWeight;
    }

    public Double getAVGAgeRangeWeight() {
        return AVGAgeRangeWeight;
    }

    public void setAVGAgeRangeWeight(Double AVGAgeRangeWeight) {
        this.AVGAgeRangeWeight = AVGAgeRangeWeight;
    }

    public Double getAVGGenderDIVWeight() {
        return AVGGenderDIVWeight;
    }

    public void setAVGGenderDIVWeight(Double AVGGenderDIVWeight) {
        this.AVGGenderDIVWeight = AVGGenderDIVWeight;
    }

    public static List<Group> getLedger() {
        return ledger;
    }
    public static void clear(){
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
        host.putWithWhomAmIEating(course,groupID);
        guest1.meetsCouple(guest2);
        guest1.meetsCouple(host);
        guest1.putWithWhomAmIEating(course,groupID);
        guest2.meetsCouple(guest1);
        guest2.meetsCouple(host);
        guest2.putWithWhomAmIEating(course,groupID);
        host.getCurrentKitchen().setUse(course);
        host.isHost();
    }

    class CouplePair{
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
}
