package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Manager {

    final GroupManager groupManager;
    final CoupleManager coupleManager;
    Location partyLoc;

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;
    List<Couple> couples;

    public Manager() {
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
        this.couples = new ArrayList<>();
    }


    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     *
     * @param path path to csv. File
     */
    public void inputPeople(String path) {
        allPersonList = CSVReader.csvReaderPeople(path);
        List<Person> singles = new ArrayList<>();

        if (allPersonList == null) {
            return;
        }
        // split people into couples and singles
        allPersonList.forEach(x -> {
            if (x.hasPartner())
                couples.add(new Couple(x, x.getPartner()));
            else
                singles.add(x);
        });
        couples.addAll(coupleManager.givePeopleWithoutPartner(singles));
        groupManager.calcGroups(couples, partyLoc);
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

}
