package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.*;
import sw.praktikum.spinfood.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Manager {
    static int managerID = 0;
    static Manager instance;
    private static final Stack<Manager> prev = new Stack<>();
    private static final Stack<Manager> future = new Stack<>();
    static int couple_Counter = 0;
    static int group_Counter = 0;
    public static int maxGuests = 999999;
    private GroupManager groupManager;
    private CoupleManager coupleManager;
    Location partyLoc = new Location(0d,0d);//temporary filler

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Person> singles = new ArrayList<>();
    List<Couple> couples = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
      //////////////////////////////////
     //   Couple-Manager Constants   //
    //////////////////////////////////

    private Strictness strictness = Strictness.B;
        /////////////////////////////////
       //   Group-Manager Constants   //
      /////////////////////////////////
     // filled with default values  //
    /////////////////////////////////
    private Double FoodPrefWeight = 5d;
    private Double AVGAgeRangeWeight = 2d;
    private Double AVGGenderDIVWeight = 1d;
    private Double distanceWeight = 3d;
    private Double optimalDistance = 10d;


    public Manager() {
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();
        this.groups = new ArrayList<>();
        instance = this;
    }
    public static Manager getInstance(){
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }
    public Manager(String path){
        inputLocation(path);
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();
        this.groups = new ArrayList<>();

    }
    // copy constructor
    private Manager(Manager manager){
        coupleManager = manager.coupleManager;
        groupManager = new GroupManager(manager.groupManager);
        allPersonList = new ArrayList<>(manager.allPersonList);
        singles = new ArrayList<>(manager.singles);
        couples = new ArrayList<>(manager.couples);
        groups = new ArrayList<>(manager.groups);
        partyLoc = new Location(manager.partyLoc);
        strictness = manager.strictness;
        FoodPrefWeight = manager.FoodPrefWeight;
        AVGAgeRangeWeight = manager.AVGAgeRangeWeight;
        AVGGenderDIVWeight = manager.AVGGenderDIVWeight;
        distanceWeight = manager.distanceWeight;
        optimalDistance = manager.optimalDistance;
    }

    public List<Person> getAllPersonList() {
        return allPersonList;
    }

    public List<Couple> getCouples() {
        return couples;
    }

    /**
     * calcAll
     * calculates all Couples first,
     * then assigns all to Groups
     */
    public void calcAll(){
        calcCouples();
        calcGroups();
    }

    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     * @param path path to csv. File
     */
    public void inputPeople(String path) {
        allPersonList = CSVReader.csvReaderPeople(path);
        GroupManager.getInstance().clear();
        groups = new ArrayList<>();
        couples = new ArrayList<>();
        singles = new ArrayList<>();
        if (allPersonList == null) {
            return;
        }
        // split people into couples and singles
        allPersonList.forEach(x -> {
            if (x.hasPartner())
                couples.add(new Couple(couple_Counter++ ,
                        x,
                        x.getPartner(),
                        x.getKitchen(),
                        x.getPartner().getKitchen(),
                        x.getCouplePreference(),
                        partyLoc));
            else
                singles.add(x);
        });
    }

    /**
     * calculates the couples using the initialized coupleManager
     */
    private void calcCouples(){
        couples = new ArrayList<>(couples.stream().filter(x-> x.getPerson1().isLockedIn()).toList());
        singles = new ArrayList<>(allPersonList.stream().filter(x -> !x.isLockedIn()).toList());
        singles.stream().peek(x -> x.setPartner(null)).toList();
        coupleManager.givePeopleWithoutPartner(singles,strictness,partyLoc);
        couples.addAll(coupleManager.getCouples());
        coupleManager.restManager();
    }
    /**
     * calculates the groups using the initialized coupleManager
     */
    public void calcGroups(){
        groupManager.clear();
        groups.clear();
        groupManager.calcGroups(couples,false);
        groups.addAll(groupManager.getLedger());
    }

    /**
     * removes both the given couples and singles
     * @param couples the couples
     * @param singles the singles
     */
    public void removeAll(List<Couple> couples,List<Person> singles){
        changedSomething();
        for (Person person : singles) {
            removePerson(person);
        }
        for (Couple couple : couples) {
            removeCouple(couple);
        }
    }

    /**
     * removePerson,
     * removes person from the couples,
     * if possible pulls a substitute from successor lists otherwise removes person and recalculates couples
     * @param person person that is to be removed
     */
    public void removePerson(Person person){
        changedSomething();
        allPersonList.remove(person);
        Couple couple = couples.stream()
                .filter(x -> x.getPerson1().equals(person) ||
                        x.getPerson2().equals(person))
                .findFirst().orElse(null);
        Person partner = person.getPartner();
        if (couple != null) {
            couples.remove(couple);
            partner.setPartner(null);
            partner.setLockedIn(false);
            singles.add(partner);
        }
        singles.remove(person);
        allPersonList.remove(person);
        calcCouples();
        GroupManager.getInstance().clear();
        groupManager.calcGroups(couples,false);
        groups = groupManager.getLedger();
    }
    /**
     * removeCouple, removes given Couple from Groups,
     * if there is a substitute replace it
     * @param couple the couple that is to be removed
     */
    public void removeCouple(Couple couple){
        changedSomething();
        couples.remove(couple);
        groupManager.remove(couple);
        groups = groupManager.getLedger();
    }

    /**
     * scans csv. file at path,
     * then instantiates a Location according to entries in file
     * assumes a header.
     *
     * @param path file path
     */
    public void inputLocation(String path) {
        partyLoc = CSVReader.csvReaderPartyLocation(path);
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public CoupleManager getCoupleManager() {
        return coupleManager;
    }

    public static int getGroupCounter(){
        return group_Counter++;
    }

    public List<Group> getGroups() {
        return groups;
    }

    /**
     * setToPrev / undo-function
     * sets to and returns the previous instance as the current image
     */
    public static void setToPrev(){
        if (prev.empty()){
            return;
        }
        future.add(new Manager(Manager.getInstance()));
        instance = prev.pop();
        GroupManager.setInstance(instance.groupManager);
        CoupleManager.setInstance(instance.coupleManager);
    }

    /**
     * setToFuture / redo-function
     * sets to and returns the last entry of the future container as the current
     */
    public static void setToFuture(){
        if (future.empty()){
            return;
        }
        prev.add(new Manager(Manager.getInstance()));
        instance = future.pop();
        GroupManager.setInstance(instance.groupManager);
        CoupleManager.setInstance(instance.coupleManager);
    }
    public Double getFoodPrefWeight() {
        return FoodPrefWeight;
    }

    public Double getAVGAgeRangeWeight() {
        return AVGAgeRangeWeight;
    }

    public Double getAVGGenderDIVWeight() {
        return AVGGenderDIVWeight;
    }

    public Double getDistanceWeight() {
        return distanceWeight;
    }

    public Double getOptimalDistance() {
        return optimalDistance;
    }

    /**
     * changedSomething()
     * needs to be called when making a change.
     * then adds container to prev stack
     * clears future stack afterward
     */
    public void changedSomething(){
        prev.add(new Manager(this));
        future.clear();
    }

    /**
     * changeParameter
     * changes the Parameters to the given values
     * @param parameterValues [maxGuests,
     *                        FoodPref,
     *                        AgeRange,
     *                        GenderDiv,
     *                        distanceWeight,
     *                        optimalDistance]
     */
    public void changeParameter(Double[] parameterValues, Strictness strictness){
        changedSomething();
        maxGuests = parameterValues[0].intValue();
        // last 5 entries for Group-Manager
        int GroupManagerIndex = parameterValues.length-5;
        FoodPrefWeight = parameterValues[GroupManagerIndex];
        AVGAgeRangeWeight = parameterValues[GroupManagerIndex + 1];
        AVGGenderDIVWeight = parameterValues[GroupManagerIndex + 2];
        distanceWeight = parameterValues[GroupManagerIndex + 3];
        optimalDistance = parameterValues[GroupManagerIndex + 4];
        this.strictness = strictness;
    }
    public static Stack<Manager> getPrev() {
        return prev;
    }
    public static Stack<Manager> getFuture() {
        return future;
    }
    public void setConfig(Double foodPrefWeight, Double AVGAgeRangeWeight, Double AVGGenderDIVWeight, Double distanceWeight, Double optimalDistance, Strictness strictness) {
        changedSomething();
        this.FoodPrefWeight = foodPrefWeight;
        this.AVGAgeRangeWeight = AVGAgeRangeWeight;
        this.AVGGenderDIVWeight = AVGGenderDIVWeight;
        this.distanceWeight = distanceWeight;
        this.optimalDistance = optimalDistance;
        this.strictness = strictness;
        calcAll();
    }
    public void saveGroupsToFile(String filePath) {
        CSVWriter.write(groups, filePath);
    }

    public Strictness getStrictness() {
        return strictness;
    }
}
