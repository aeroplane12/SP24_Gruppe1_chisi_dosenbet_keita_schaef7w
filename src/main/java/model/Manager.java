package model;

import model.tools.*;
import java.util.List;

public class Manager {

    GroupManager groupManager;
    CoupleManager coupleManager;
    Location partyLoc;

    //For testing purposes
    List<Person> personTestSet;

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
        //file with custom header
        personTestSet = CSVReader.csvReaderPeople(path);
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
