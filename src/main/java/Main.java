import model.*;
import model.tools.CSVWriter;


public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        // CSV-Writer Test
        /*
        Kitchen kitchen1 = new Kitchen(3.2, 4.4, false, 3.2);
        Kitchen kitchen2 = new Kitchen(3.4, 4.6, false, 3.2);
        Location partyLoc = new Location(421.2, 51.0);
        Person person1 = new Person("123", "ahahahaha", AgeGroup.AgeRange.AGE_0_17, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, kitchen1, null);
        Person person2 = new Person("321", "oioioioio", AgeGroup.AgeRange.AGE_0_17, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, kitchen2, person1);
        person1.setPartner(person2);
        Couple couple1 = new Couple(1512431 ,person1, person2, kitchen1, kitchen2, FoodPreference.FoodPref.MEAT, partyLoc);
        Group group1 = new Group(couple1, null, null ,Course.DINNER,15231);
         */
        manager.calcAll();
        CSVWriter.write(manager.getGroups(), "Dokumentation/TestingData/teilnehmerlisteProcessed.csv");
    }
}
