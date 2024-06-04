package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    static int couple_Counter = 0;
    static int group_Counter = 0;
    GroupManager groupManager;
    CoupleManager coupleManager;
    Location partyLoc;

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Person> singles;
    List<Couple> couples;
    List<Group> groups;

    public Manager(GroupManager groupManager, CoupleManager coupleManager){
        this.groupManager = groupManager;
        this.coupleManager = coupleManager;
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
                        x.getCouplePreference()));
            else
                singles.add(x);
        });
        // TODO: Remove singles && people who dont have a person go back to singles
        couples.addAll(coupleManager.givePeopleWithoutPartner(singles));

        groupManager.calcGroups();
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
