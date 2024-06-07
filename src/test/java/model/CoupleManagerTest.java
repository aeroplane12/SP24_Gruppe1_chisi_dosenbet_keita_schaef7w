package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CoupleManagerTest {
    private CoupleManager coupleManager;
    private Person person1, person2, person3, person4;
    private List<Person> singles;

    @BeforeEach
    public void setUp() {
        // TODO: Initialize coupleManager with appropriate data
        coupleManager = CoupleManager.getInstance();
        coupleManager.setStrictnessLevel(0);
        coupleManager.getSinglesList().clear(); // Ensure the list is empty before each test

        Kitchen kitchen1 = new Kitchen(10.0, 20.0, false, 1.0);
        Kitchen kitchen2 = new Kitchen(30.0, 40.0, true, 2.0);

        person1 = new Person("1", "Alice", AgeGroup.getAgeRange("25"), Gender.genderValue.female, FoodPreference.getFoodPref("VEGAN"), kitchen1, null);
        person2 = new Person("2", "Bob", AgeGroup.getAgeRange("30"), Gender.genderValue.male, FoodPreference.getFoodPref("MEAT"), kitchen2, null);
        person3 = new Person("3", "Charlie", AgeGroup.getAgeRange("35"), Gender.genderValue.male, FoodPreference.getFoodPref("VEGGIE"), kitchen1, null);
        person4 = new Person("4", "Diana", AgeGroup.getAgeRange("28"), Gender.genderValue.female, FoodPreference.getFoodPref("NONE"), kitchen2, null);

        singles = new ArrayList<>();
        singles.add(person1);
        singles.add(person2);
        singles.add(person3);
        singles.add(person4);
    }

    @Test
    public void testCalcCouples() {
        // TODO: Implement this test method once calcCouples is implemented
        coupleManager.givePeopleWithoutPartner(singles, 1);
        List<Couple> couples = coupleManager.couples;
        assertNotNull(couples);
    }

    @Test
    public void testAddPerson() {
        // TODO: Implement this test method once addPerson is implemented
        coupleManager.addPerson(person1);
        assertTrue(coupleManager.getSinglesList().contains(person1));
    }

    @Test
    public void testRemovePerson() {
        // TODO: Implement this test method once removePerson is implemented
        coupleManager.addPerson(person1);
        coupleManager.removeSinglePerson(person1.getID());
        assertFalse(coupleManager.getSinglesList().contains(person1));
    }

    @Test
    public void testGetPerson() {
        // TODO: Implement this test method once getPerson is implemented
        coupleManager.addPerson(person1);
        Person retrievedPerson = coupleManager.getSinglePerson(person1.getID());
        assertEquals(person1, retrievedPerson);
    }

    @Test
    public void testGetCouple() {
        // TODO: Implement this test method once getCouple is implemented
        coupleManager.givePeopleWithoutPartner(singles, 0);
        List<Couple> couples = coupleManager.couples;
        assertNotNull(couples);
    }
}