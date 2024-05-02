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

    public void csvReaderPeople(String path) {
        //default file with header
        csvReaderPeople(path, true);
    }
    public void csvReaderPeople(String path, boolean header) {
        Scanner scanner;
        //Temp shit
        List<Person> testPerson = new ArrayList<>();
        List<Couple> testCouple = new ArrayList<>();
        try {
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            //skip the first line
            String[] input;
            if (scanner.hasNext() && header) {
                scanner.nextLine();
            }

            while (scanner.hasNext()) {
                input = scanner.nextLine().split("[,\n]", -1);
                if (input[10].isEmpty()) {
                    testPerson.add(new Person(input));
                } else {
                    testCouple.add(new Couple(input));
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void csvReaderPartyLocation(String path){
        csvReaderPartyLocation(path,true);
    }
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
}
