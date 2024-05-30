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
    // min allowed distance between Groups ("community-detection")
    private float minAllowedDistance;

    // Import of satisfying a meaties desire to eat meat or
    // a Veggies desire to consume Animal Products,
    // [0-2] meaning 0-ignore, 1-if-able and 2-paramount
    private int satisfactionWeight;
    // "at what point can you use the emergency kitchens"
    private int kitchenMaybeWeight;

    private static GroupManager instance;
    //|-----------------------------|



    //All the Couples and their respective instances
    Map<String,Person[]> couples;
    //Matrix of every Couple and whom they meet at each point in time
    Map<String,Person[][]> allGroupClusters; //KEY = coupleID, VALUE = 4 People in 3 rows each

    public static GroupManager getInstance() {
        if(instance == null)
            instance = new GroupManager();

        return instance;
    }

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


}
