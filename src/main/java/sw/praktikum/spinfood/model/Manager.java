package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.*;
import sw.praktikum.spinfood.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Manager {
    static Manager instance;
    private final Stack<Manager> prev = new Stack<>();
    private final Stack<Manager> future = new Stack<>();
    static int couple_Counter = 0;
    static int group_Counter = 0;
    public static int maxGuests = 666666;
    private GroupManager groupManager;
    private CoupleManager coupleManager;
    Location partyLoc;//temporary filler

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Person> singles = new ArrayList<>();
    List<Couple> couples = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    /////////////////////////////////
    //   Group-Manager Constants   //
    /////////////////////////////////

    private Strictness strictness;
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
        instance = this;
    }
    public static Manager getInstance(){
        if (instance == null) {
            return new Manager();
        }
        return instance;
    }
    public Manager(String path){
        inputLocation(path);
        this.groupManager = GroupManager.getInstance();
        this.groupManager.setPartyLoc(partyLoc);
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();

    }
    // copy constructor
    private Manager(Manager manager){
        coupleManager = manager.coupleManager;
        groupManager = new GroupManager(manager.groupManager);
        allPersonList = new ArrayList<>(manager.allPersonList);
        singles = new ArrayList<>(manager.singles);
        couples = new ArrayList<>(manager.couples);
        groups = new ArrayList<>(manager.groups);

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
        couples = new ArrayList<>(couples.stream().filter(x->!x.getPerson1().isLockedIn()).toList());
        singles = new ArrayList<>(allPersonList.stream().filter(x -> !x.hasPartner()).collect(Collectors.toList()));
        coupleManager.givePeopleWithoutPartner(singles,strictness,couple_Counter,partyLoc);
        couples.addAll(coupleManager.getCouples());
        singles = coupleManager.getSingleList();
        couple_Counter = coupleManager.getCurrentCoupleCount();
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
        coupleManager.removeSinglePerson(person);
        singles = coupleManager.getSingleList();
        couples = coupleManager.getCouples();
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
        groupManager.setPartyLoc(partyLoc);
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
    public void setToPrev(){
        future.add(new Manager(this));
        instance = prev.pop();
    }

    /**
     * setToFuture / redo-function
     * sets to and returns the last entry of the future container as the current
     */
    public void setToFuture(){
        prev.add(new Manager(this));
        instance = future.pop();
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
     * afterwords clears future stack
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

}
