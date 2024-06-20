package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Manager {
    static Manager instance;
    private Stack<Container> prev;
    private Stack<Container> future;
    static int couple_Counter = 0;
    static int group_Counter = 0;
    public static int maxGuests = 400;
    GroupManager groupManager;
    CoupleManager coupleManager;
    Location partyLoc;//temporary filler

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Person> singles = new ArrayList<>();
    List<Couple> couples = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
      /////////////////////////////
     // Group-Manager Constants //
    /////////////////////////////

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
        GroupManager.setPartyLoc(partyLoc);
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();

    }

    /**
     * calcAll
     * calculates all Couples first,
     * then assigns all to Groups
     */
    public void calcAll(){
        calcCouples();
    }

    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     * @param path path to csv. File
     */
    public void inputPeople(String path) {
        allPersonList = CSVReader.csvReaderPeople(path);

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
        coupleManager.givePeopleWithoutPartner(singles,0,couple_Counter,partyLoc);
        couples.addAll(coupleManager.getCouples());
        singles = coupleManager.getSingleList();
        couple_Counter = coupleManager.getCurrentCoupleCount();
        calcGroups();
    }
    /**
     * calculates the groups using the initialized coupleManager
     */
    public void calcGroups(){
        groupManager.calcGroups(couples,false);
        groups.addAll(groupManager.getLedger());
    }

    /**
     * removes both the given couples and singles
     * @param couples the couples
     * @param singles the singles
     */
    public void removeAll(List<Couple> couples,List<Person> singles){
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
        coupleManager.removeSinglePerson(person);
        singles = coupleManager.getSingleList();
        couples = coupleManager.getCouples();
        GroupManager.clear();
        groupManager.calcGroups(couples,false);
        groups = groupManager.getLedger();
    }
    /**
     * removeCouple, removes given Couple from Groups,
     * if there is a substitute replace it
     * @param couple the couple that is to be removed
     */
    public void removeCouple(Couple couple){
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
        GroupManager.setPartyLoc(partyLoc);
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
     * sets to and returns the previous container
     * @return the current container
     */
    public Container setToPrev(){
        // adds the current status as a container to the future stack
        future.add(new Container(allPersonList,
                couples,
                groups,
                //GROUP MANAGER STUFF
                FoodPrefWeight,
                AVGAgeRangeWeight,
                AVGGenderDIVWeight,
                distanceWeight,
                optimalDistance,
                GroupManager.getInstance().succeedingCouples,
                GroupManager.getInstance().overBookedCouples));
        Container curr = prev.pop();
        switchTO(curr);
        return curr;
    }
    private void switchTO(Container container){
        allPersonList = container.getPersonList();
        couples = container.getCoupleList();
        groups = container.getGroupList();
        // INPUT COUPLE MANAGER CONSTANTS HERE
        // START GROUP MANAGER CONSTANTS
        FoodPrefWeight = container.getFoodPrefWeight();
        AVGAgeRangeWeight = container.getAVGAgeRangeWeight();
        AVGGenderDIVWeight = container.getAVGGenderDIVWeight();
        distanceWeight = container.getDistanceWeight();
        optimalDistance = container.getOptimalDistance();
        GroupManager.getInstance().overBookedCouples = container.getOverBookedCouples();
        GroupManager.getInstance().succeedingCouples = container.getSucceedingCouples();
        GroupManager.getInstance().ledger = container.getGroupList();
        // END GROUP MANAGER CONSTANTS

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
     * changedSomething
     * needs to be called when making a change.
     * method creates container
     * then adds container to prev stack
     * afterwords clears future stack
     */
    public void changedSomething(){
        prev.add(new Container(allPersonList,
                couples,
                groups,
                //GROUP MANAGER STUFF
                FoodPrefWeight,
                AVGAgeRangeWeight,
                AVGGenderDIVWeight,
                distanceWeight,
                optimalDistance,
                GroupManager.getInstance().succeedingCouples,
                GroupManager.getInstance().overBookedCouples));
        future.clear();
    }

}
