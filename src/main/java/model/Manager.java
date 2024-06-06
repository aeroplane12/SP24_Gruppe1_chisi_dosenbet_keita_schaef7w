package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    static int couple_Counter = 0;
    static int group_Counter = 0;
    GroupManager groupManager;
    CoupleManager coupleManager;
    Location partyLoc;//temporary filler

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Person> singles = new ArrayList<>();
    List<Couple> couples = new ArrayList<>();
    List<Group> groups;

    public Manager(GroupManager groupManager, CoupleManager coupleManager){
        this.groupManager = groupManager;
        this.coupleManager = coupleManager;
    }
    public Manager(GroupManager groupManager, CoupleManager coupleManager,String path){
        this.groupManager = groupManager;
        this.coupleManager = coupleManager;
        inputLocation(path);
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
        couples.addAll(coupleManager.givePeopleWithoutPartner(singles));
        singles = coupleManager.getSinglesList();

        //groupManager.calcGroups(couples);
    }

    /**
     * scans csv. file at path,
     * then instantiates a Location according to entries in file
     * assumes a header.
     * @param path file path
     */
    public void inputLocation(String path){
        partyLoc = CSVReader.csvReaderPartyLocation(path);
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public CoupleManager getCoupleManager() {
        return coupleManager;
    }

}
