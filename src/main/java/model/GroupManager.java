package model;

import model.tools.Rankable;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class GroupManager {
    private Double FOODPREFWEIGHT;
    private Double AVGAGERANGEWEIGHT;
    private Double AVGGENDERDIVWEIGHT;
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
                0 : (x.getFoodPref().value - y.getFoodPref().value)) * FOODPREFWEIGHT
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG()) * AVGAGERANGEWEIGHT
                + Math.pow(x.getCurrentKitchen().distance(y.getCurrentKitchen())-optimalDistance,2)* distanceWeight
                - Math.abs(x.getGenderAVG() - y.getGenderAVG()) * AVGGENDERDIVWEIGHT);
    public GroupManager(){
        FOODPREFWEIGHT = 1d;
        AVGAGERANGEWEIGHT = 1d;
        AVGGENDERDIVWEIGHT = 1d;
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
        FOODPREFWEIGHT = foodPrefWeight;
        AVGAGERANGEWEIGHT = avgAgeRangeWeight;
        AVGGENDERDIVWEIGHT = avgGenderDiffWeight;
        this.optimalDistance = optimalDistance;
        this.distanceWeight =distanceWeight;
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

        List<Couple> possibleHosts = resolveKitchenConflicts(new ArrayList<>(allCouples));
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
        // first mapping all  kitchen to their owners
        kitchenLedger = new HashMap<>(allCouples.stream().collect(groupingBy(Couple::getCurrentKitchen)));
        // removing all kitchen with less than three owners
        for (List<Couple> couples : kitchenLedger.values()){
            int size = couples.size();
            if (size > 3) {
                for (Couple c : couples) {
                    if (c.getOtherKitchen()!=null && !c.getOtherKitchen().equals(c.getCurrentKitchen())) {
                        //if the partner has another kitchen, and it's not also overbooked, use it
                        kitchenLedger.get(c.getCurrentKitchen()).remove(c);
                        c.toggleWhoseKitchen();
                        kitchenLedger.get(c.getCurrentKitchen()).add(c);
                        size--;
                    }
                }
            }
            while (size > 3) {
                // removes all conflicting entries until there are no more than three
                allCouples.remove(couples.get(0));
                overBookedCouples.add(couples.get(0));
                kitchenLedger.get(couples.get(0).getCurrentKitchen()).remove(couples.get(0));
                size--;
            }
        }
        return allCouples;
    }
    /**
     * fillCourse
     * assigns everyone a group
     * @param course time at which the group gets together
     * @param hosts all possible hosts
     * @return the Groups associated with the course
     */
    public List<Group> fillCourse(List<Couple> hosts, Course course){
        List<Group> assortedGroups = new ArrayList<>();
        while (!hosts.isEmpty()) {
            Couple host = hosts.stream().filter(x->!x.WasHost() &&
                    !x.getCurrentKitchen().checkUse(course)).toList().get(0);
            assortedGroups.add(findGuests(host,
                    new ArrayList<>(hosts.stream().filter(x->!x.equals(host)).toList()),
                    course));
            hosts.removeAll(assortedGroups.get(assortedGroups.size()-1).getAll());
        }
        return assortedGroups;
    }

    /**
     * findGuests
     * @param host the host
     * @param possibleMatches all possible matches
     * @param course the time
     * @return the formed group
     */
    public Group findGuests(Couple host, List<Couple> possibleMatches, Course course){
        List<Couple> potentialGuests = new ArrayList<>(possibleMatches.stream()
                .filter(x->!x.getMetCouple().contains(host))
                .sorted((x,y) ->{
                    int z = Boolean.compare(x.getCurrentKitchen().checkUse(course), y.getCurrentKitchen().checkUse(course));
                    if (z != 0) {
                        return z;
                    }
                    return COUPLERANKGEN.rank(host,x).compareTo(COUPLERANKGEN.rank(host,y));
                }).toList());
        potentialGuests.removeIf(x->x.getMetCouple().stream().anyMatch(potentialGuests::contains));
        Couple g1 = potentialGuests.get(0);
        Couple g2 = potentialGuests.get(1);
        Group group = new Group(host,g1,g2,course,Manager.getGroupCounter());
        // toggling all flags
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

    public static List<Group> getLedger() {
        return ledger;
    }
}
