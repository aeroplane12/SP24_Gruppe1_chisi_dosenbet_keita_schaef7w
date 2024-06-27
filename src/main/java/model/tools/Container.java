package model.tools;

import model.Couple;
import model.Group;
import model.Person;

import java.util.ArrayList;
import java.util.List;

public class Container {

    private final List<Person> PERSON_LIST;
    private final List<Couple> COUPLE_LIST;
    private final List<Group> GROUP_LIST;
    private final int MAX_GUESTS;

    //-CONSTANTS FOR GROUP MANAGER-
    private final Double FOOD_PREF_WEIGHT;
    private final Double AVG_AGE_RANGE_WEIGHT;
    private final Double AVG_GENDER_DIV_WEIGHT;
    private final Double DISTANCE_WEIGHT;
    private final Double OPTIMAL_DISTANCE;
    private final List<Couple> SUCCEEDING_COUPLES;
    private final List<Couple> OVERBOOKED_COUPLES;
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
        PERSON_LIST = new ArrayList<>(personList);
        COUPLE_LIST = new ArrayList<>(coupleList);
        GROUP_LIST = new ArrayList<>(groupList);
        MAX_GUESTS = mG;
        //GROUP MANAGER HERE
        FOOD_PREF_WEIGHT = FPW;
        AVG_AGE_RANGE_WEIGHT = avgARW;
        AVG_GENDER_DIV_WEIGHT = avgGDW;
        DISTANCE_WEIGHT = dW;
        OPTIMAL_DISTANCE = oD;
        SUCCEEDING_COUPLES = new ArrayList<>(sCGM);
        OVERBOOKED_COUPLES = new ArrayList<>(oBCGM);
    }

    public List<Person> getPERSON_LIST() {
        return PERSON_LIST;
    }


    public List<Couple> getCOUPLE_LIST() {
        return COUPLE_LIST;
    }


    public List<Group> getGROUP_LIST() {
        return GROUP_LIST;
    }


    public Double getDISTANCE_WEIGHT() {
        return DISTANCE_WEIGHT;
    }

    public Double getAVG_GENDER_DIV_WEIGHT() {
        return AVG_GENDER_DIV_WEIGHT;
    }

    public Double getAVG_AGE_RANGE_WEIGHT() {
        return AVG_AGE_RANGE_WEIGHT;
    }

    public Double getFOOD_PREF_WEIGHT() {
        return FOOD_PREF_WEIGHT;
    }

    public Double getOPTIMAL_DISTANCE() {
        return OPTIMAL_DISTANCE;
    }

    public List<Couple> getOVERBOOKED_COUPLES() {
        return OVERBOOKED_COUPLES;
    }

    public List<Couple> getSUCCEEDING_COUPLES() {
        return SUCCEEDING_COUPLES;
    }

    public int getMAX_GUESTS() {return MAX_GUESTS;}
}
