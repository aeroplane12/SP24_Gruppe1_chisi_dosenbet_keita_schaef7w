import model.*;
import model.tools.CSVWriter;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager Manager = new Manager();
        Manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        Manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        // CSV-Writer Test
        Kitchen kitchen1 = new Kitchen(3.2, 4.4, false, 3.2);
        Kitchen kitchen2 = new Kitchen(3.4, 4.6, false, 3.2);
        Location partyLoc = new Location(421.2, 51.0);
        Person person1 = new Person("123", "ahahahaha", AgeGroup.AgeRange.AGE_0_17, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, kitchen1, null);
        Person person2 = new Person("321", "oioioioio", AgeGroup.AgeRange.AGE_0_17, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, kitchen2, person1);
        person1.setPartner(person2);
        Couple couple1 = new Couple(1512431 ,person1, person2, kitchen1, kitchen2, FoodPreference.FoodPref.MEAT, partyLoc);
        Group group1 = new Group(couple1, null, null ,Course.DINNER,15231);
        ArrayList<Group> groups = new ArrayList<>(2);
        groups.add(group1);
        groups.add(group1);
        CSVWriter.write(groups, "Dokumentation/TestingData/outputTest.csv");
    }
}
