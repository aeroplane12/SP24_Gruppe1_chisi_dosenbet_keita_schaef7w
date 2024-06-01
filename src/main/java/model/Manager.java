package model;

import model.tools.*;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    final GroupManager groupManager;
    final CoupleManager coupleManager;
    Location partyLoc;

    // maximum distance between kitchens for it to be measured as equal in meters
    public static final Double MAX_EQUAL_KITCHEN_DISTANCE = 0d;
    List<Person> allPersonList;

    public Manager(){
        this.groupManager = GroupManager.getInstance();
        this.coupleManager = CoupleManager.getInstance();
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
        giveSingleParticipants();
        groupManager.calcGroups();
    }

    private void giveSingleParticipants() {
        List<Person> singles = allPersonList.stream().filter(x -> !x.hasPartner()).toList();
        coupleManager.givePeopleWithoutPartner(singles);

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
