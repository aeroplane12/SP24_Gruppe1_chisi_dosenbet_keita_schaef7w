package model;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    Manager manger;
    List<Person> person;

    @BeforeEach
    void setUp() {
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        person = new ArrayList<>();
        this.manger = new Manager(groupManager, coupleManager);

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
            person.add(newPerson);
        }
    }




    @Test
    public void testCoupleManager() {
        CoupleManager coupleManager = manger.getCoupleManager();
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

        coupleManager.removePerson(partner);
        //Only removes the partner not test person
        assertEquals(coupleManager.allParticipants.length, 1);
    }

    @Test
    public void testGroupManager() {


    }


}