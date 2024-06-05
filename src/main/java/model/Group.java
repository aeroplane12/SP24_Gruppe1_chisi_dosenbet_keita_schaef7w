package model;

public class Group {
    private final int ID;

    Person[] hosts;
    Person[] guests;
    float avgGender;
    float avgAgeRange;
    FoodPreference.FoodPref foodPreference;
    public Group(Person[] host, int id){
        ID = id;
    }

    public int getID() {
        return ID;
    }
}
