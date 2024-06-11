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
    public List<Couple> processedCouples = new ArrayList<>(); //just for testing
    private Double distanceWeight;
    private Double optimalDistance;
    private static GroupManager instance;
    private final static List<Group> ledger = new ArrayList<>();
    private Map<Kitchen,List<Couple>> kitchenLedger = new HashMap<>();
    private Location partyLoc;
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value == 0 || y.getFoodPref().value == 0? 0 : (x.getFoodPref().value - y.getFoodPref().value)) * FoodPrefWeight
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * AVGAgeRangeWeight
                + Math.pow(x.getCurrentKitchen().distance(y.getCurrentKitchen())-optimalDistance,2) * distanceWeight
                - (getGenderDiversity(x.getGenderDiv(),y.getGenderDiv())) * AVGGenderDIVWeight);
    public GroupManager(){
        // empty constructor
        FoodPrefWeight = 5d;
        AVGAgeRangeWeight = 2d;
        AVGGenderDIVWeight = 1d;
        distanceWeight = 3d;
        // distance in meters
        optimalDistance = 100d;
        // needs to be set for it to work unless the party happens at Null island
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
        /*DONE sorting couples into Groups according to specifications
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
        while (possibleHosts.size()%9 != 0) {
            // removes the most conflicting and furthest away kitchen Couples first
            // until it reaches a point at which there are possible solutions
            succeedingCouples.add(possibleHosts.remove(0));
            kitchenLedger.get(succeedingCouples.get(succeedingCouples.size()-1).getCurrentKitchen())
                    .remove(succeedingCouples.get(succeedingCouples.size()-1));
        }

        //sort all couples from closest to furthest away from party-Location
        possibleHosts.sort((x,y) ->
                x.getCurrentKitchen()
                .distance(partyLoc)
                .compareTo(y.getCurrentKitchen()
                        .distance(partyLoc)));
        //O(n*log(n)+n*log(n))=O(n*log(n))
        processedCouples = new ArrayList<>(possibleHosts);
        //O(n*log(n)+n) = O(n*log(n))
        // need everyone from conflicting kitchen to host first, then sort by distance
        for (Course course : new Course[]{Course.DESSERT,Course.DINNER,Course.STARTER}) {
            ledger.addAll(fillCourse(new ArrayList<>(possibleHosts),course));
            //O(n^3*log(n))
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
        kitchenLedger = new HashMap<>();
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

        return findValidCouplePairMatching(//O(n^2+n^3*log(n)) = O(n^3*log(n))
                couplePairList,
                guests,
                course,
                new ArrayList<>());
    }

    /**
     * findValidCouplePairMatching
     * iterates through each CouplePair Combo and assigns
     * to the least matchable pair their best possible match recursively
     * @param hostGuestCombo tbe hosting Couple and their best guest possible
     * @param allRemainingCouples everyone who isn't already included in hostGuestCombo
     * @param course the time
     * @param groups the groups / the output
     * @return all groups for the specified time
     */
    List<Group> findValidCouplePairMatching(List<CouplePair> hostGuestCombo, List<Couple> allRemainingCouples, Course course,List<Group> groups){
        //runtime Complexity of findValidCouplePairMatching,
        // given hostGuestCombo and allRemainingCouples both have the same size of n/3,
        // therefore we'll define n/3 as m
        if (allRemainingCouples.isEmpty()) { //O(1)
            return groups;
        }
        Map<CouplePair,List<Couple>> hostMatchMap = new HashMap<>(hostGuestCombo.stream()
                .collect(Collectors
                        .toMap(Function.identity(),//O(m)
                                // Host-Couple pairs mapped to lowest ranking remaining Couples
                                x -> new ArrayList<>(allRemainingCouples.stream()
                                        .filter(y->!x.history.contains(y))// O(m)
                                        .sorted((a,b)->COUPLERANKGEN.rank(x.host,a).compareTo(COUPLERANKGEN.rank(x.host,b))) //O(m*log(m))
                                        .toList()))));
        //O(m*(m+m*log(m))) = O(m^2*log(m))
        Map.Entry<CouplePair,List<Couple>> leastMatchablePair = hostMatchMap.entrySet()
                .stream()
                .min(Comparator.comparingInt(x -> x.getValue().size()))//O(m)
                .orElseThrow();
        //O(m+m^2*log(m))= O(m^2*log(m))
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
        //O(1+m^2*log(m))= O(m^2*log(m))
        //add the group to the output, remove the guest and the pair
        groups.add(group);//O(1)
        hostGuestCombo.remove(leastMatchablePair.getKey());//O(m)
        allRemainingCouples.remove(guest2);//O(m)
        //O(1+2m+m^2*log(m))= O(m^2*log(m))
        return findValidCouplePairMatching(
                hostGuestCombo,
                allRemainingCouples,
                course,
                groups);
        //O(m*m^2*log(m))= O(m^3*log(m))
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
        getInstance().succeedingCouples.clear();
        getInstance().overBookedCouples.clear();
        getInstance().kitchenLedger.clear();
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
        host.isHost();
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

    public static void setPartyLoc(Location partyLoc) {
        instance.partyLoc = partyLoc;
    }
}
