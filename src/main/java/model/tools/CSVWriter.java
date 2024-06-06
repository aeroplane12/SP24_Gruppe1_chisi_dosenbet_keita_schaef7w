package model.tools;

import model.Couple;
import model.Course;
import model.Group;

import java.io.File;
import java.util.Collection;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;

public class CSVWriter {
    public static void write(List<Group> groups, String fileName) {
        File file = new File(fileName); // Create new file instance with the filepath
        try {
            FileWriter fileWriter = new FileWriter(file); // Create new fileWriter with file
            for (int i = 0; i < groups.size(); i++) {
                Couple couple = groups.get(i).getHosts(); // Couple obj of the hosts
                // Write Person 1 and Person 2 into CSV
                fileWriter.write(couple.getPerson1().getName() + ";" + couple.getPerson2().getName() + ";");
                // Write if couple registered together or not
                if (couple.getPerson1().isLockedIn()) {
                    fileWriter.write("1;");
                } else {
                    fileWriter.write("0;");
                }
                // Write longitude and latitude of the used kitchen
                boolean whoseKitchen = couple.isWhoseKitchen();
                if (whoseKitchen) {
                    fileWriter.write(couple.getKitchen2().getLongitude().toString() + ";" + couple.getKitchen2().getLatitude().toString() + ";");
                } else {
                    fileWriter.write(couple.getKitchen1().getLongitude().toString() + ";" + couple.getKitchen1().getLatitude().toString() + ";");
                }
                // Write food preference of the couple
                fileWriter.write(couple.getFoodPref().toString() + ";");
                // Write couple ID
                fileWriter.write(couple.getID() + ";");
                // Write starter, dinner and dessert group ID
                fileWriter.write(couple.getWithWhomAmIEating().get(Course.STARTER) + ";" + couple.getWithWhomAmIEating().get(Course.DINNER) + ";" + couple.getWithWhomAmIEating().get(Course.DESSERT) + ";");
                // WWrite which persons kitchen is used
                if (whoseKitchen) {
                    fileWriter.write("true" + ";");
                } else {
                    fileWriter.write("false" + ";");
                }
                // Write which course the couple cooks
                fileWriter.write(groups.get(i).getCourse().value + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
