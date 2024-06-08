package model.tools;

import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static model.Manager.MAX_EQUAL_KITCHEN_DISTANCE;

public class CSVReader {
    //CSV_CONSTANTS
    private final static String SEPARATOR = ",";
    private final static String NEWLINE = "\n";

    // Maps with Header names and index of Entry
    private final static Map<String,Integer> PEOPLE_PARSE_INDEX = new HashMap<>(
            Map.ofEntries(
                    Map.entry("Index",0),
                    Map.entry("ID",1),
                    Map.entry("Name",2),
                    Map.entry("FoodPreference",3),
                    Map.entry("Age",4),
                    Map.entry("Sex",5),
                    Map.entry("Kitchen",6),
                    Map.entry("Kitchen_Story",7),
                    Map.entry("Kitchen_Longitude",8),
                    Map.entry("Kitchen_Latitude",9),
                    Map.entry("ID_2",10),
                    Map.entry("Name_2",11),
                    Map.entry("Age_2",12),
                    Map.entry("Sex_2",13)));

    private final static Map<String,Integer> LOCATION_PARSE_INDEX = new HashMap<>(
            Map.ofEntries(
                    Map.entry("Longitude",0),
                    Map.entry("Latitude",1)));


    /**
     * Reads .csv File at Path,
     * then transforms entries into People
     * @param path path to csv. File
     */
    public static List<Person> csvReaderPeople(String path) {
        Scanner scanner;
        List<Person> output = new ArrayList<>();
        String[] input;
        try {
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            if (!scanner.hasNext()) {
                return null; // returns null if file empty
            }
            //checking first line for header
            input = scanner.nextLine().split("["+SEPARATOR+NEWLINE+"]", -1);
            if (PEOPLE_PARSE_INDEX.containsKey(input[1])) {
                for (int i = 0; i<input.length;i++) {
                    // replace default assignment with current
                    PEOPLE_PARSE_INDEX.put(input[i].isEmpty()?"Index":input[i],i);
                }
            } else {
                //not pretty but functional
                Person n = new Person(new String[]{
                        input[PEOPLE_PARSE_INDEX.get("Index")],
                        input[PEOPLE_PARSE_INDEX.get("ID")],
                        input[PEOPLE_PARSE_INDEX.get("Name")],
                        input[PEOPLE_PARSE_INDEX.get("FoodPreference")],
                        input[PEOPLE_PARSE_INDEX.get("Age")],
                        input[PEOPLE_PARSE_INDEX.get("Sex")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Story")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Longitude")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Latitude")],
                        input[PEOPLE_PARSE_INDEX.get("ID_2")],
                        input[PEOPLE_PARSE_INDEX.get("Name_2")],
                        input[PEOPLE_PARSE_INDEX.get("Age_2")],
                        input[PEOPLE_PARSE_INDEX.get("Sex_2")]});
                output.add(n);
                if (n.hasPartner()) {
                    output.add(n.getPartner());
                }
            }

            while (scanner.hasNext()) {
                input = scanner.nextLine().split("["+SEPARATOR+NEWLINE+"]", -1);
                Person n = new Person(new String[]{
                        input[PEOPLE_PARSE_INDEX.get("Index")],
                        input[PEOPLE_PARSE_INDEX.get("ID")],
                        input[PEOPLE_PARSE_INDEX.get("Name")],
                        input[PEOPLE_PARSE_INDEX.get("FoodPreference")],
                        input[PEOPLE_PARSE_INDEX.get("Age")],
                        input[PEOPLE_PARSE_INDEX.get("Sex")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Story")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Longitude")],
                        input[PEOPLE_PARSE_INDEX.get("Kitchen_Latitude")],
                        input[PEOPLE_PARSE_INDEX.get("ID_2")],
                        input[PEOPLE_PARSE_INDEX.get("Name_2")],
                        input[PEOPLE_PARSE_INDEX.get("Age_2")],
                        input[PEOPLE_PARSE_INDEX.get("Sex_2")]});
                for (Person j : output) {
                    if (n.getKitchen() != null &&
                            j.getKitchen() != null &&
                            n.getKitchen() != j.getKitchen() &&
                            j.getKitchen().distance(n.getKitchen()) < MAX_EQUAL_KITCHEN_DISTANCE) {
                        j.getKitchen().mergeKitchen(n.getKitchen());
                    }
                    /* redundant, changed person constructor
                    if (n.hasPartner() &&
                            n.getPartner().getKitchen() != null &&
                            j.getKitchen() != null &&
                            n.getPartner().getKitchen() != j.getKitchen() &&
                            j.getKitchen().distance(n.getPartner().getKitchen()) < MAX_EQUAL_KITCHEN_DISTANCE) {
                        j.getKitchen().mergeKitchen(n.getPartner().getKitchen());
                    }
                     */
                }
                output.add(n);
                //if (n.hasPartner()) {
                //    output.add(n.getPartner());
                //}

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }


    /**
     * scans csv. file at path,
     * then instantiates a Location according to entries in file
     * @param path file path
     */
    public static Location csvReaderPartyLocation(String path){
        Scanner scanner;
        Location output;
        try{
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            String[] input;
            if (!scanner.hasNext()) {
                return null; // returns null if file empty
            }
            //checking first line for header
            input = scanner.nextLine().split("["+SEPARATOR+NEWLINE+"]", -1);
            if (LOCATION_PARSE_INDEX.containsKey(input[1])) {
                for (int i = 0; i < input.length; i++) {
                    // replace default assignment with current
                    LOCATION_PARSE_INDEX.put(input[i], i);
                }
                input = scanner.nextLine().split("["+SEPARATOR+NEWLINE+"]", -1);
            }
            output = new Location(
                    Double.parseDouble(input[LOCATION_PARSE_INDEX.get("Longitude")]),
                    Double.parseDouble(input[LOCATION_PARSE_INDEX.get("Latitude")]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}
