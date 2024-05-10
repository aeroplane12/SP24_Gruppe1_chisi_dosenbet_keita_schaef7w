package model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
     * then transforms entries into People,
     * will assume a header in file
     * @param path path to csv. File
     */
    public void csvReaderPeople(String path) {
        //default file with header
        csvReaderPeople(path, true);
    }
    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     * @param path path to csv. File
     * @param header whether the given file has a header
     */
    public void csvReaderPeople(String path, boolean header) {
        Scanner scanner;
        //Temporary, simply for Console output
        personTestSet = new ArrayList<>();
        String[] input;
        try {
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            //skip first line
            if (scanner.hasNext() && header) {
                scanner.nextLine();
            }

            while (scanner.hasNext()) {
                input = scanner.nextLine().split("[,\n]", -1);
                Person n = new Person(input);
                personTestSet.add(n);
                if (n.hasPartner()) {
                    personTestSet.add(n.getPartner());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        personTestSet.forEach(System.out::println);
    }

    /**
     * scans csv. file at path,
     * then instantiates a Location according to entries in file
     * assumes a header.
     * @param path file path
     */
    public void csvReaderPartyLocation(String path){
        csvReaderPartyLocation(path,true);
    }
    /**
     * scans csv. file at path,
     * then instantiates a Location according to entries in file
     * @param path file path
     * @param header whether the file has a header
     */
    public void csvReaderPartyLocation(String path,boolean header){
        Scanner scanner;
        try{
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            String[] input;
            if (scanner.hasNext() && header) {
                scanner.nextLine();
            }
            input = scanner.nextLine().split("[,\n]");
            partyLoc = new Location(Double.parseDouble(input[0]),Double.parseDouble(input[1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public CoupleManager getCoupleManager() {
        return coupleManager;
    }

}
