package model;


import java.util.Arrays;

public class Person {
    private final String ID;
    private String name;
    private FoodPreference.FoodPref foodPreference;
    private AgeGroup.AgeRange age;
    private Gender.g gender;
    private Kitchen kitchen;
    private Person partner; // pointer to a Partner
    private boolean lockedIn;     // whether the partner is locked (previously assigned)

    /**
     * constructor for creating a Person through the .csv file
     * @param strings the raw input already segregated into strings
     */
    public Person(String[] strings){
        ID = strings[1];
        name = strings[2];
        foodPreference = FoodPreference.getFoodPref(strings[3]);
        age = AgeGroup.getAgeRange(strings[4]);
        gender = Gender.getGen(strings[5]);
        kitchen = strings[6].equals("no") || strings[6].isEmpty() ? null: new Kitchen(Arrays.copyOfRange(strings,6,10));
        if (!strings[10].isEmpty()) {
            lockedIn = true;
            partner = new Person(
                    Arrays.copyOfRange(strings, 10, 14),
                    foodPreference,
                    kitchen,
                    this);
        } else {
            partner = null;
            lockedIn = false;
        }

    }

    /**
     * Constructor for generating a Partner from one .csv entry
     * @param strings Raw input consisting of ID NAME AGE GENDER
     * @param food the food preference of the couple
     * @param k the kitchen of their partnership (might be redundant)
     * @param partner pointer to partner
     */
    public Person(String[] strings, FoodPreference.FoodPref food, Kitchen k,Person partner){
        ID = strings[0];
        name = strings[1];
        age = AgeGroup.getAgeRange(strings[2]);
        gender = Gender.getGen(strings[3]);
        foodPreference = food;
        kitchen = k;
        this.partner = partner;
        lockedIn = true;
    }

    // getter methods
    public FoodPreference.FoodPref getFoodPreference() {
        return foodPreference;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public Person getPartner() {
        return partner;
    }

    /**
     * getCouple
     * retrieves the Partner, and this Person,
     * if this Person is not assigned a Partner, returns NULL
     * @return the Couple or null
     */
    public Person[] getCouple(){
        if (partner == null){
            return null;
        }
        return new Person[]{this,partner};
    }

    @Override
    public String toString() {
        return "[PersonID: " + ID +
                ", PersonName: " + name +
                (partner != null ? ", PartnerID: " +
                        partner.getID() +
                        ", PartnerName: " +
                        partner.getName() :
                        ", Partner: none ") +
                ", FoodPreference: " +
                foodPreference +
                ", Age: " +
                age +
                ", Gender: " +
                gender +
                ", Kitchen" +
                (kitchen != null ?
                        kitchen.toString():
                        "none ") +
                "]";
    }
}
