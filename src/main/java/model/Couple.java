package model;

public class Couple {

    private Person person1;

    private Person person2;

    private Kitchen kitchen1;
    private Kitchen kitchen2;
    FoodPreference.FoodPref foodPref;
    public Couple(Person person1, Person person2,
                  Kitchen kitchen1, Kitchen kitchen2,
                  FoodPreference.FoodPref foodPref) {
        this.person1 = person1;
        this.person2 = person2;
        this.kitchen1 = kitchen1;
        this.kitchen2 = kitchen2;
        this.foodPref = foodPref;
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


}
