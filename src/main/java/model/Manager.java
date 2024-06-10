package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
    List<Group> groups = new ArrayList<>();

    public Manager() {
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();
    }
    public Manager(String path){
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();
        inputLocation(path);
    }


    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     *
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
        coupleManager.givePeopleWithoutPartner(singles, 0, couple_Counter++, partyLoc);
        couples.addAll(coupleManager.getCouples());
        singles = coupleManager.getStillSingleList();
        couple_Counter = coupleManager.getCurrentCoupleCount();
        groupManager.calcGroups(couples);

        groups.addAll(GroupManager.getLedger());
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
}
