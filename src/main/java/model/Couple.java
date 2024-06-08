package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Couple {

    private int ID;

    // Integer is the Group ID

    private final Map<Course,Integer> withWhomAmIEating = new HashMap<>(Map.of(
            Course.STARTER,-1,
            Course.DINNER,-1,
            Course.DESSERT,-1));

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
        }
        if (kitchen1 == null || kitchen2 == null) {
            whoseKitchen = kitchen1 == null;
        } else {
            whoseKitchen = kitchen1.distance(partyLoc) > kitchen2.distance(partyLoc);
        }

    }
    public int getID() {
        return ID;
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

    public void toggleWhoseKitchen() {
        whoseKitchen = !whoseKitchen;
    }

    public Map<Course, Integer> getWithWhomAmIEating() {
        return withWhomAmIEating;
    }
    public boolean putWithWhomAmIEating(Course course, int id) {
        int out = withWhomAmIEating.put(course,id);
        if (out != -1) {
            System.out.println("ALARM!!");
        }
        return -1 == out;
    }
    public Kitchen getCurrentKitchen(){
        if (kitchen1==null && kitchen2==null) {
            throw new NullPointerException("what the fuck");
        }
        if (kitchen2==null) {
            whoseKitchen = false;
            return kitchen1;
        }
        if (kitchen1==null){
            whoseKitchen = true;
            return kitchen2;
        }

        return whoseKitchen? kitchen2:kitchen1;
    }
    public Kitchen getOtherKitchen(){
        return !whoseKitchen? kitchen2:kitchen1;
    }

    public Set<Couple> getMetCouple() {
        return metCouple;
    }
    public void meetsCouple(Couple couple){
        metCouple.add(couple);
    }

    public Double getGenderAVG() {
        return (person1.getGender().value+person2.getGender().value)/2d;
    }
    public Double getAgeRAngeAVG(){
        return (person1.getAge().value+person2.getAge().value)/2d;
    }

    public boolean WasHost() {
        return wasHost;
    }

    public void isHost() {
        this.wasHost = true;
    }

}
