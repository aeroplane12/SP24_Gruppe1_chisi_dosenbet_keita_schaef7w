package sw.praktikum.spinfood.model;


import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Person {
    private final String ID;
    private String name;
    private FoodPreference.FoodPref foodPreference;
    private final HashSet<Person> notAllowed = new HashSet<>();
    private AgeGroup.AgeRange age;
    private Gender.genderValue gender;
    private Kitchen kitchen;
    private Person partner; // pointer to a Partner
    private boolean lockedIn;     // whether the partner is locked (registered as a Couple)

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
        kitchen = strings[6].equals("no") || strings[6].isEmpty() ?
                null:
                new Kitchen(Arrays.copyOfRange(strings,6,10));
        if (kitchen!=null) {
            //TODO needs a way of adding / merging a co-owned kitchen
            // rn. it merely adds an owner to a new kitchen
            // probably a job for the general manager
            kitchen.addOwner(this);
        }
        if (!strings[10].isEmpty()) {
            lockedIn = true;
            partner = new Person(
                    Arrays.copyOfRange(strings, 10, 14),
                    foodPreference,
                    null,
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

    /**
     * Constructor for testing purposes and fine-tuning
     * @param ID ID of person
     * @param name name of person
     * @param age the kitchen of their partnership (might be redundant)
     * @param gender pointer to partner
     * @param food the food preference of the person
     * @param kitchen the kitchen the person is using
     * @param partner the reference to the partner of the person
     */
    public Person (String ID, String name, AgeGroup.AgeRange age,
                   Gender.genderValue gender , FoodPreference.FoodPref food,
                   Kitchen kitchen,Person partner) {
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.foodPreference = food;
        this.kitchen = kitchen;
        this.partner = partner;
    }
    // setter methods
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(AgeGroup.AgeRange age) {
        if (age == null) {
            this.age = AgeGroup.AgeRange.AGE_24_27;
        } else {this.age = age;}
    }
    public void setGender(Gender.genderValue gender) {
        if (gender == null) {
            this.gender = Gender.genderValue.other;
        } else {
            this.gender = gender;
        }
    }
    public void setFoodPreference(FoodPreference.FoodPref foodPreference) {
        if (foodPreference == null) {
            this.foodPreference = FoodPreference.FoodPref.NONE;
        } else {this.foodPreference = foodPreference;}
    }
    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }
    public void setPartner(Person partner) {
        this.partner = partner;
    }
    public void setLockedIn(boolean lockedIn) {
        this.lockedIn = lockedIn;
    }

    // getter methods

    public FoodPreference.FoodPref getFoodPreference() {
        return foodPreference;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }
    public Double getKitchenLongitude() {
        return kitchen == null? null : kitchen.getLongitude();
    }
    public Double getKitchenLatitude() {
        return kitchen == null? null : kitchen.getLatitude();
    }
    public Double getKitchenStory() {
        return kitchen == null? null : kitchen.getStory();
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

    public boolean hasKitchen() {return kitchen != null; }

    public AgeGroup.AgeRange getAge() {
        return age;
    }

    public Gender.genderValue getGender() {
        return gender;
    }

    public boolean isLockedIn() {
        return lockedIn;
    }

    /**
     * hasPartner
     * @return whether person has partner
     */
    public boolean hasPartner(){
        return partner!=null;
    }
    /**
     * getCouple
     * retrieves the Partner, and this Person,
     * if this Person is not assigned a Partner, returns NULL
     * @return the Couple or null
     */
    public Person[] getCouple(){
        return hasPartner()? new Person[]{this,partner}:null;
    }

    /**
     * getCoupleIDs
     * creates a CoupleID by concatenating both peopleIDs in a set order
     * hopefully unique
     * @return a String consisting of both personalIDs
     */
    public String getCoupleIDs(){
        return hasPartner()?Arrays.stream(new String[]{ID, partner.getID()})
                .sorted()
                .collect(Collectors.joining(","))
                :null;
    }

    /**
     * getCoupleKitchens
     * @return gets you the Kitchen of both participants if able
     */
    public Kitchen[] getCoupleKitchens(){
        return new Kitchen[]{kitchen,
                hasPartner()?
                        getPartner().getKitchen():
                        null};
    }

    /**
     * getCouplePreference
     * @return the FoodPreference of the couple
     */
    public FoodPreference.FoodPref getCouplePreference(){
        if (partner.foodPreference.equals(FoodPreference.FoodPref.NONE)) {
            return foodPreference;
        } else if (foodPreference.equals(FoodPreference.FoodPref.NONE)) {
            return partner.foodPreference;
        } else if (foodPreference.equals(FoodPreference.FoodPref.VEGAN) ||
                partner.foodPreference.equals(FoodPreference.FoodPref.VEGAN)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (foodPreference.equals(FoodPreference.FoodPref.VEGGIE)||
                partner.foodPreference.equals(FoodPreference.FoodPref.VEGGIE)){
            return FoodPreference.FoodPref.VEGGIE;
        } else {
            return FoodPreference.FoodPref.MEAT;
        }
    }


    @Override
    public String toString() {
        return "[PersonID: " + ID +
                ", PersonName: " + name +
                (hasPartner()?
                        ", PartnerID: " + partner.getID() + ", PartnerName: " + partner.getName() :
                        ", Partner: none") +
                ", FoodPreference: " + foodPreference +
                ", Age: " + age +
                ", Gender: " +
                gender +
                ", Kitchen: " +
                (kitchen != null ?
                        kitchen.toString():
                        "none") +
                "]";
    }
}
