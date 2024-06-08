package model;

import java.util.List;

public class Group {
    private final int ID;

    private final Couple HOST;
    private final Couple GUEST1;
    private final Couple GUEST2;
    FoodPreference.FoodPref foodPreference;
    Course course;
    public Group(Couple host,
                 Couple guest1,
                 Couple guest2,
                 Course course,
                 int id){
        HOST = host;
        GUEST1 = guest1;
        GUEST2 = guest2;
        ID = id;
        int hostFoodPref = host.getFoodPref().value;
        int guest1FoodPref = GUEST1.getFoodPref().value;
        int guest2FoodPref = GUEST2.getFoodPref().value;

        if (hostFoodPref + guest1FoodPref + guest2FoodPref == 0) {
            foodPreference = FoodPreference.FoodPref.MEAT;
        } else if (hostFoodPref == 3 || guest1FoodPref == 3 || guest2FoodPref == 3) {
            foodPreference = FoodPreference.FoodPref.VEGAN;
        } else if (hostFoodPref == 2 || guest1FoodPref == 2 || guest2FoodPref == 2) {
            foodPreference = FoodPreference.FoodPref.VEGGIE;
        } else {
            foodPreference = FoodPreference.FoodPref.MEAT;
        }
        this.course = course;
    }

    public int getID() {
        return ID;
    }
    public Couple getHosts() {
        return HOST;
    }
    public Course getCourse() {
        return course;
    }
    public List<Couple> getAll(){
        return List.of(HOST,GUEST1,GUEST2);
    }
}
