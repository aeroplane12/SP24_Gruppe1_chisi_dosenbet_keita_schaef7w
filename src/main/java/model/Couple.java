package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Couple {

    private int ID;


    private Map<Course,Integer> withWhomAmIEating = new HashMap<>(Map.of(
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
    private Course hosts;
    private Set<Couple> metCouples = new HashSet<>(Set.of(this));
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

    /**
     * putWithWhomAmIEating
     * @param course the time
     * @param id the GroupID,
     *          with which this Couple is associated
     */
    public void putWithWhomAmIEating(Course course, int id) {
        withWhomAmIEating.put(course,id);
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

    public Set<Couple> getMetCouples() {
        return metCouples;
    }

    /**
     * checks whether the given couple has already been met
     * @param couple the couple in question
     * @return true or false
     */
    public boolean checkMetCouple(Couple couple) {
        return metCouples.contains(couple);
    }

    /**
     * setting the given Couple a met
     * @param couple the couple this.couple is meeting right now :P
     */
    public void meetsCouple(Couple couple){
        metCouples.add(couple);
    }

    // Values associated with the calculation of Ranks
    public Set<Gender.genderValue> getGenderDiv() {
        Set<Gender.genderValue> out = new HashSet<>(Set.of(getPerson1().getGender()));
        out.add(getPerson2().getGender());
        return out;
    }
    public Double getAgeRAngeAVG(){
        return (person1.getAge().value+person2.getAge().value)/2d;
    }

    /**
     * wasHost()
     * @return whether this Couple has already hosted a Group
     */
    public boolean wasHost() {
        return wasHost;
    }
    /**
     * isHost
     * sets the wasHost flag to true
     */
    public void isHost() {
        this.wasHost = true;
    }
    public void setHosts(Course time){
        hosts = time;
    }
    public Course getHosts(){
        return hosts;
    }
    public void setMetCouples(Set<Couple> metCouples){
        this.metCouples = metCouples;
    }
    public void clearFlags(){
        wasHost = false;
        metCouples = new HashSet<>(Set.of(this));
        hosts = null;
        withWhomAmIEating = new HashMap<>(Map.of(
                Course.STARTER,-1,
                Course.DINNER,-1,
                Course.DESSERT,-1));
    }
}
