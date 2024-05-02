package model;

import java.util.Map;

public class GroupManager {
    //|---CALCULATION - VARIABLES---|
    // the highest allowed average AgeRange distribution
    private float maxAvgAgeRange;
    // the lowest allowed average Gender distribution
    private float minGenderDistribution;
    // maximal allowed distance between Groups
    private int maxAllowedDistance;

    // Import of satisfying a meaties desire to eat meat or
    // a Veggies desire to consume Animal Products,
    // [0-2] meaning 0-ignore, 1-if-able and 2-paramount
    private int satisfactionWeightV;
    private int satisfactionWeightM;
    //|-----------------------------|



    //All the Couples and their respective instances
    Map<String,Person[]> couples;
    //Matrix of every Couple and whom they meet at each point in time
    Map<String,Person[][]> allGroupClusters; //KEY = coupleID, VALUE = 4 People in 3 rows each
    public void calcGroups(){
        //TODO sorting couples into Groups according to specifications
        // -first by location, small distances but closing in on partyLoc
        // -second by food preference, meaties with meaties and nones ,veggies with vegans and other veggies
        // -third by age, keeping the ageRange minimal
    }
    private Group[] generateGroup(){
        return null;
    }
    public Person[][] printCluster(String coupleID){
        //TODO returns the Cluster centered around the given couple.
        return null;        //3x3 matrix
    }
    public static class Group extends Location{
        Person host;
        Person[] guests;
        float avgGender;
        float avgAgeRange;
        FoodPreference.FoodPref foodPreference;
        public Group(Person host){
            super(host.getKitchen().longitude,host.getKitchen().latitude);
        }
    }


}
