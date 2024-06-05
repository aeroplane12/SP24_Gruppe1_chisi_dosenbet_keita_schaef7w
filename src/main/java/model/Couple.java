package model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Couple {

    private int ID;

    // Integer is the Group ID

    private final Map<Course,Integer> withWhomAmIEating = Map.of(
            Course.STARTER,-1,
            Course.DINNER,-1,
            Course.DESSERT,-1);

    private Person person1;

    private Person person2;

    private Kitchen kitchen1;

    private Kitchen kitchen2;
    private FoodPreference.FoodPref foodPref;
    private boolean whoseKitchen; //whose kitchen is used, can change
    //-Field-Parameters for Group-Manager-
    private boolean wasHost = false;
    private Set<Couple> metCouple = new HashSet<>();
    //-Field-Parameters for Group-Manager- End

    public Couple(int ID,
                  Person person1, Person person2,
                  Kitchen kitchen1, Kitchen kitchen2,
                  FoodPreference.FoodPref foodPref,Location partyLoc) {
        this.person1 = person1;
        this.person2 = person2;
        this.kitchen1 = kitchen1;
        this.kitchen2 = kitchen2;
        this.foodPref = foodPref;
        this.ID = ID;
        //needs to have one kitchen, only one can be null
        if (partyLoc == null) {
            whoseKitchen = false; //simply a default value, will delete if partyLoc always notNull
        } else if (kitchen1 == null || kitchen2 == null) {
            whoseKitchen = kitchen1 == null;
        } else {
            whoseKitchen = kitchen1.distance(partyLoc) > kitchen2.distance(partyLoc);
        }

    }


    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public Kitchen getKitchen1() {
        return kitchen1;
    }

    public void setKitchen1(Kitchen kitchen1) {
        this.kitchen1 = kitchen1;
    }

    public Kitchen getKitchen2() {
        return kitchen2;
    }

    public void setKitchen2(Kitchen kitchen2) {
        this.kitchen2 = kitchen2;
    }

    public FoodPreference.FoodPref getFoodPref() {
        return foodPref;
    }

    public void setFoodPref(FoodPreference.FoodPref foodPref) {
        this.foodPref = foodPref;
    }


    public boolean isWhoseKitchen() {
        return whoseKitchen;
    }

    public void setWhoseKitchen(boolean whoseKitchen) {
        this.whoseKitchen = whoseKitchen;
    }

    public Map<Course, Integer> getWithWhomAmIEating() {
        return withWhomAmIEating;
    }
}
