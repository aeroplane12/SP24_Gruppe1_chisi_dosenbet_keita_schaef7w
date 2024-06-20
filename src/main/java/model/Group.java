package model;

import java.util.List;

public class Group {
    private final int ID;

    private Couple host;
    private Couple guest1;
    private Couple guest2;
    FoodPreference.FoodPref foodPreference;
    Course course;
    public Group(Couple host,
                 Couple guest1,
                 Couple guest2,
                 Course course,
                 int id){
        this.host = host;
        this.guest1 = guest1;
        this.guest2 = guest2;
        ID = id;
        int hostFoodPref = this.host.getFoodPref().value;
        int guest1FoodPref = this.guest1.getFoodPref().value;
        int guest2FoodPref = this.guest2.getFoodPref().value;

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
        return host;
    }
    public Course getCourse() {
        return course;
    }
    public List<Couple> getAll(){
        return List.of(host,guest1,guest2);
    }
    public void replaceCouple(Couple couple, Couple replacement){
        if (couple.equals(host)) {
            host = replacement;
        } else if (couple.equals(guest1)) {
            guest1 = replacement;
        } else if (couple.equals(guest2)) {
            guest2 = replacement;
        }

        // reassign FoodPreference, just in case
        int hostFoodPref = this.host.getFoodPref().value;
        int guest1FoodPref = this.guest1.getFoodPref().value;
        int guest2FoodPref = this.guest2.getFoodPref().value;

        if (hostFoodPref + guest1FoodPref + guest2FoodPref == 0) {
            foodPreference = FoodPreference.FoodPref.MEAT;
        } else if (hostFoodPref == 3 || guest1FoodPref == 3 || guest2FoodPref == 3) {
            foodPreference = FoodPreference.FoodPref.VEGAN;
        } else if (hostFoodPref == 2 || guest1FoodPref == 2 || guest2FoodPref == 2) {
            foodPreference = FoodPreference.FoodPref.VEGGIE;
        } else {
            foodPreference = FoodPreference.FoodPref.MEAT;
        }

    }
}
