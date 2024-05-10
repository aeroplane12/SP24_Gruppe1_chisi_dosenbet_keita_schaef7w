package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    Manager manager;
    CoupleManager coupleManager;
    GroupManager groupManager;
    List<Person> persons;

    @BeforeEach
    void setUp() {
        this.coupleManager = new CoupleManager();
        this.groupManager = new GroupManager();
        persons = new ArrayList<>();
        this.manager = new Manager(groupManager, coupleManager);

        AgeGroup.AgeRange age1 = AgeGroup.AgeRange.AGE_31_35;
        AgeGroup.AgeRange age2 = AgeGroup.AgeRange.AGE_18_23;
        Gender.genderValue male = Gender.genderValue.male;
        Gender.genderValue female = Gender.genderValue.female;
        String[] names = {"James", "Bob", "Mary", "Sam", "Alex", "Mathew",
                "Foster", "Dobby", "Harry", "Bond", "Robert", "Tom",
                "Schweinsteiger", "Messi", "Ronaldo", "Mcgregor", "Gane", "Alessio",
                "Matt", "Max", "Neuer", "Kimmich", "Leory", "Lahm",
                "Götze", "Fox", "Alice", "Amanda", "Kathrin", "Eva",
                "Andreas", "Felix", "Kieta", "Tobias", "Paciants", "Anna",
                "Saka", "Rice", "Riccardo", "Zidane", "Joshua", "Peter",
                "Xhaka", "Frimpong", "Allen", "Alonso", "Xavi", "Drogba",
                "Essen", "Paris", "Winter", "Summer", "Autumn", "Spring",};

        String[] ID = {"1", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23", "24",
                "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36",
                "37", "38", "39", "40", "41", "42",
                "43", "44", "45", "46", "47", "48",
                "49", "50", "51", "52", "53", "54",};

        Kitchen[] kitchens = new Kitchen[27];
        for (int i = 0; i < 27; i++) {
            double length = 15.00 + i;
            double width = 34.00 + i;
            boolean hasOven = i % 2 == 0; // alternate between true and false
            double area = 3.0 + i;
            kitchens[i] = new Kitchen(length, width, hasOven, area);
        }

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String id = ID[i];
            AgeGroup.AgeRange age = (i % 2 == 0) ? age1 : age2; // alternate between two age groups
            Gender.genderValue gender = (i % 2 == 0) ? male : female; // alternate between male and female
            FoodPreference.FoodPref foodPref = FoodPreference.FoodPref.MEAT; // assuming all persons prefer meat
            Kitchen kitchen = kitchens[i % 27]; // use kitchens in a round-robin fashion
            Person partner = null; // assuming no partner initially

            Person newPerson = new Person(id, name, age, gender, foodPref, kitchen, partner);
            persons.add(newPerson);
        }
        // Setting one partner
        persons.get(1).setPartner(persons.get(3));
    }

    @Test
    public void testCoupleManager() {
        CoupleManager coupleManager = manager.getCoupleManager();
        AgeGroup.AgeRange age = AgeGroup.AgeRange.AGE_31_35;
        Gender.genderValue gender = Gender.genderValue.male;
        FoodPreference.FoodPref foodPref = FoodPreference.FoodPref.MEAT;
        Kitchen kitchen = new Kitchen(15.00, 34.00, true, 3.0);

        Person partner = new Person("2", "alice", age, gender, foodPref, kitchen, null);
        Person testPerson = new Person("1", "Bob", age, gender, foodPref, kitchen, partner);
        partner.setPartner(testPerson);

        coupleManager.addPerson(testPerson);
        //As person has partner both should be added
        assertEquals(coupleManager.allParticipants.length, 2);
        assertEquals(coupleManager.getCouple(testPerson.getCoupleIDs())[0].getCoupleIDs(),
                "1,2");
        coupleManager.removePerson(partner.getID());
        //Only removes the partner not test person
        assertEquals(coupleManager.allParticipants.length, 1);
        assertEquals(testPerson, coupleManager.getPerson("1"));
    }

    @Test
    public void testGroupManager() {
        /* TODO GroupManagerTests
        coupleManager.calcCouples();
        coupleManager.giveToGroupManager(groupManager);
        String ID = "1"; //TODO: Default ID think about how to implement ID Couple
        int size = groupManager.couples.get(ID).length;

        assertEquals(size,coupleManager.allParticipants.length / 2, "The size is the same");
        // Check if couple from CV file is correct

        assertEquals(coupleManager.getCouple("ID Couple"), "ID Couple");
         */
    }
     @Test
        public void testCsvReaderPeople() {
        // TODO: Implement this test method once you know what to expect from csvReaderPeople
         String filePath = "teilnehmerliste.csv";
         Manager manager = new Manager(new GroupManager(), new CoupleManager());
         manager.csvReaderPeople(filePath);
        }

        @Test
        public void testCsvReaderPartyLocation() {
            // TODO: Implement this test method once you know what to expect from csvReaderPartyLocation
            String filePath = "partylocation.csv";
            Manager manager = new Manager(new GroupManager(), new CoupleManager());
            manager.csvReaderPartyLocation(filePath);
        }

        @Test
        public void testGetGroupManager() {

            assertEquals(groupManager, manager.getGroupManager());
        }

        @Test
        public void testGetCoupleManager() {
            assertEquals(coupleManager, manager.getCoupleManager());
        }
}