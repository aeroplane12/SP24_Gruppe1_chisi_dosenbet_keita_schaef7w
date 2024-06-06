package model;

import model.tools.Rankable;

import java.util.*;
import java.util.stream.Collectors;

public class GroupManager {
    private Double FOODPREFWEIGHT;
    private Double AVGAGERANGEWEIGHT;
    private Double AVGGENDERDIVWEIGHT;
    private List<Couple> allCouples;
    private List<Couple> succeedingCouples;
    private Map<Course,List<Group>> ledger = new HashMap<>(Map.of(
            Course.DESSERT,new ArrayList<>(),
            Course.DINNER,new ArrayList<>(),
            Course.STARTER,new ArrayList<>()));
    private Location partyLoc;
    private final Rankable<Couple> COUPLERANKGEN = ((x,y)->
        Math.abs(x.getFoodPref().value - y.getFoodPref().value) * FOODPREFWEIGHT
                + Math.abs(x.getAgeRAngeAVG() - y.getAgeRAngeAVG())*AVGAGERANGEWEIGHT
                - Math.abs(x.getGenderAVG() - y.getGenderAVG()) * AVGGENDERDIVWEIGHT);
    public GroupManager(){
        FOODPREFWEIGHT = 0d;
        AVGAGERANGEWEIGHT = 0d;
        AVGGENDERDIVWEIGHT = 0d;
    }
    public GroupManager(Double foodprefweight,
                        Double maxavgagerange,
                        Double mingenderdistribution) {
        FOODPREFWEIGHT = foodprefweight;
        AVGAGERANGEWEIGHT = maxavgagerange;
        AVGGENDERDIVWEIGHT = mingenderdistribution;
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
        List<Couple> possibleHosts = this.allCouples;
        possibleHosts.sort( (x,y) ->
                x.getCurrentKitchen()
                        .distance(partyLoc)
                        .compareTo(y.getCurrentKitchen()
                                .distance(partyLoc)));
        for (Couple c : possibleHosts) {

        }

    }

    /**
     * fillCourse
     * fills the maü
     */
    private void fillCourse(){

    }

    private Group findGuests(Couple host){
        List<Couple> potentialGuests = allCouples.stream()
                .filter(x->!(x.getMetCouple().contains(host) && x.equals(host)))
                .toList();
        // HUNGARIAN ALGORITHM HERE
        return null;
    }

}
