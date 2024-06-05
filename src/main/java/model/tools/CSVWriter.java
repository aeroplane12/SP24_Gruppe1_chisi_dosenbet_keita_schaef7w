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
        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (int i = 0; i < groups.size(); i++) {
                Couple couple = groups.get(i).getHosts();
                fileWriter.write(couple.getPerson1().getName() + ";" + couple.getPerson2().getName() + ";");
                if (couple.getPerson1().isLockedIn()) {
                    fileWriter.write("1;");
                } else {
                    fileWriter.write("0;");
                }
                boolean whoseKitchen = couple.isWhoseKitchen();
                if (whoseKitchen) {
                    fileWriter.write(couple.getKitchen2().getLongitude().toString() + ";" + couple.getKitchen2().getLatitude().toString() + ";");
                } else {
                    fileWriter.write(couple.getKitchen1().getLongitude().toString() + ";" + couple.getKitchen1().getLatitude().toString() + ";");
                }
                fileWriter.write(couple.getFoodPref().toString() + ";");
                fileWriter.write(couple.getWithWhomAmIEating().get(Course.STARTER) + ";" + couple.getWithWhomAmIEating().get(Course.DINNER) + ";" + couple.getWithWhomAmIEating().get(Course.DESSERT) + ";");
                if (whoseKitchen) {
                    fileWriter.write(2 + ";");
                } else {
                    fileWriter.write(1 + ";");
                }
                fileWriter.write(groups.get(i).getCourse().value + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
