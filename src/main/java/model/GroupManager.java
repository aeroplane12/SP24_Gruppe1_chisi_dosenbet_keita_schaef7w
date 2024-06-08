package model;

import model.tools.Rankable;

import java.util.*;


public class GroupManager {
    private Double FoodPrefWeight;
    private Double AVGAgeRangeWeight;
    private Double AVGGenderDIVWeight;
    public List<Couple> succeedingCouples = new ArrayList<>();
    public List<Couple> overBookedCouples = new ArrayList<>(); //couples that are sadly not usable unless a conflicting couple gets deleted
    private Double distanceWeight;
    private Double optimalDistance;
    private static GroupManager instance;
    private static List<Group> ledger = new ArrayList<>();
    private Map<Kitchen,List<Couple>> kitchenLedger;
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
            kitchenLedger.get(succeedingCouples.get(succeedingCouples.size()-1).getCurrentKitchen())
                    .remove(succeedingCouples.get(succeedingCouples.size()-1));
        }
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
        for (Couple couple : allCouples) {
            Kitchen currKitchen = couple.getCurrentKitchen();

            if (!kitchenLedger.containsKey(currKitchen)) {
                //if there are no kitchen like this in the register
                kitchenLedger.put(currKitchen,new ArrayList<>(List.of(couple)));
            } else if (kitchenLedger.get(couple.getCurrentKitchen()).size() <= 3){
                // if there are less than 3 kitchen of that type present
                kitchenLedger.get(couple.getCurrentKitchen()).add(couple);
            } else {

                if (couple.getOtherKitchen()!=null) {
                    //if there is another kitchen
                    if (kitchenLedger.get(couple.getOtherKitchen()).size() < 3) {
                        // and there are less than 3 kitchen in this registry
                        kitchenLedger.get(currKitchen).remove(couple);
                        couple.toggleWhoseKitchen();
                        if (kitchenLedger.containsKey(couple.getCurrentKitchen())) {
                            // if the other kitchen is already in the ledger, add to it
                            kitchenLedger.get(couple.getCurrentKitchen()).add(couple);
                        } else {
                            // else
                            kitchenLedger.put(couple.getCurrentKitchen(),new ArrayList<>(List.of(couple)));
                        }
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
        List<Group> assortedGroups = new ArrayList<>();// the output
        int stepSize = participants.size()/3;
        List<Couple> determinedHosts = participants.stream().filter(x->!x.WasHost() &&
                x.getCurrentKitchen().checkAndSetUser(course, x.getID()))
                .limit(stepSize).toList();
        List<Couple> guests = new ArrayList<>(participants);
        guests.removeAll(determinedHosts);
        for (Couple host :determinedHosts) {
            assortedGroups.add(findGuests(host, guests, course));
            guests.removeAll(assortedGroups.get(assortedGroups.size()-1).getAll());
        }

        return assortedGroups;
    }

    /**
     * findGuests
     * @param host the host
     * @param possibleGuests all possible matches of people who already hosted
     * @param course the time
     * @return the formed group
     */
    public Group findGuests(Couple host, List<Couple> possibleGuests, Course course){
        List<Couple> potentialGuests = new ArrayList<>(possibleGuests.stream()
                .filter(x->!x.getMetCouple().contains(host))
                .sorted((x,y) ->COUPLERANKGEN.rank(host,x).compareTo(COUPLERANKGEN.rank(host,y))).toList());
        Couple g1 = null;
        Couple g2 = null;
        for (Couple i : potentialGuests){
            for (Couple j: potentialGuests) {
                if (!i.getMetCouple().contains(j) && j.getID() != i.getID()) {
                    g1 = i;
                    g2 = j;
                    break;
                }
            }
            if (g1 != null && g2 != null){
                break;
            }
        }
        if (g1 == null || g2 == null) {
            throw new RuntimeException("no Group could be Created with given Guests and Host");
        }

        Group group = new Group(host,g1,g2,course,Manager.getGroupCounter());
        // toggling all flags
        host.meetsCouple(g1);
        host.meetsCouple(g2);
        host.putWithWhomAmIEating(course,group.getID());
        host.getCurrentKitchen().setUse(course);
        host.isHost();
        g1.meetsCouple(g2);
        g1.meetsCouple(host);
        g1.putWithWhomAmIEating(course,group.getID());
        g2.meetsCouple(g1);
        g2.meetsCouple(host);
        g2.putWithWhomAmIEating(course, group.getID());
        //not very efficient but hopefully functional
        return group;
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

}
