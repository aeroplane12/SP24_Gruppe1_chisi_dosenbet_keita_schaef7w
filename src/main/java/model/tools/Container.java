package model.tools;

import model.Couple;
import model.Group;
import model.Person;

import java.util.List;

public class Container {

    private List<Person> personList;
    private List<Couple> coupleList;
    private List<Group> groupList;
    private int maxGuests;

    //-CONSTANTS FOR GROUP MANAGER-
    private Double FoodPrefWeight;
    private Double AVGAgeRangeWeight;
    private Double AVGGenderDIVWeight;
    private Double distanceWeight;
    private Double optimalDistance;
    private List<Couple> succeedingCouples;
    private List<Couple> overBookedCouples;
    //-----------------------------

    public Container(List<Person> personList,
                     List<Couple> coupleList,
                     List<Group> groupList,
                     int mG,
                     Double FPW,
                     Double avgARW,
                     Double avgGDW,
                     Double dW,
                     Double oD,
                     List<Couple> sCGM,
                     List<Couple> oBCGM) {
        this.personList = personList;
        this.coupleList = coupleList;
        this.groupList = groupList;
        maxGuests = mG;
        //GROUP MANAGER HERE
        FoodPrefWeight =FPW;
        AVGAgeRangeWeight = avgARW;
        AVGGenderDIVWeight = avgGDW;
        distanceWeight = dW;
        optimalDistance = oD;
        succeedingCouples = sCGM;
        overBookedCouples = oBCGM;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Couple> getCoupleList() {
        return coupleList;
    }

    public void setCoupleList(List<Couple> coupleList) {
        this.coupleList = coupleList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public Double getDistanceWeight() {
        return distanceWeight;
    }

    public Double getAVGGenderDIVWeight() {
        return AVGGenderDIVWeight;
    }

    public Double getAVGAgeRangeWeight() {
        return AVGAgeRangeWeight;
    }

    public Double getFoodPrefWeight() {
        return FoodPrefWeight;
    }

    public Double getOptimalDistance() {
        return optimalDistance;
    }

    public List<Couple> getOverBookedCouples() {
        return overBookedCouples;
    }

    public List<Couple> getSucceedingCouples() {
        return succeedingCouples;
    }
}
