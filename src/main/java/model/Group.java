package model;

import java.util.List;

public class Group {
    private final int ID;

    private final Couple HOST;
    private final List<Couple> GUESTS;
    float avgGender;
    float avgAgeRange;
    FoodPreference.FoodPref foodPreference;
    Course course;
    public Group(Couple host,List<Couple> guests, int id,Course course){
        HOST = host;
        GUESTS = guests;
        ID = id;
    }

    public int getID() {
        return ID;
    }
}
