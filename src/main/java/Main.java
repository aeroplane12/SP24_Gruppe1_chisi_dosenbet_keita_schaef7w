import model.*;
import model.tools.CSVReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        Manager Manager = new Manager(groupManager, coupleManager);
        Manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        Manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        List<Person> personList = CSVReader.csvReaderPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        List<Person> filteredList = personList.stream().filter(elem -> elem.getFoodPreference() == FoodPreference.FoodPref.NONE).toList();
        System.out.println(personList.get(0).getGender().value == personList.get(5).getGender().value);
    }
}
