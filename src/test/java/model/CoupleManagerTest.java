package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CoupleManagerTest {
    private CoupleManager coupleManager;
    private Person person1, person2, person3, person4, person5, person6;
    private Couple couple1, couple2;
    private List<Person> singles;
    private Location loc = new Location(0d,0d);

    @BeforeEach
    public void setUp() {
        // TODO: Initialize coupleManager with appropriate data
        coupleManager = CoupleManager.getInstance();
        coupleManager.setStrictnessLevel(0);
        coupleManager.getSingleList().clear(); // Ensure the list is empty before each test

        Kitchen kitchen1 = new Kitchen(10.0, 20.0, false, 1.0);
        Kitchen kitchen2 = new Kitchen(30.0, 40.0, true, 2.0);

        person1 = new Person("1", "Alice", AgeGroup.getAgeRange("25"), Gender.genderValue.female, FoodPreference.getFoodPref("VEGAN"), kitchen1, person2);
        person2 = new Person("2", "Bob", AgeGroup.getAgeRange("30"), Gender.genderValue.male, FoodPreference.getFoodPref("MEAT"), kitchen2, person1);
        person3 = new Person("3", "Charlie", AgeGroup.getAgeRange("35"), Gender.genderValue.male, FoodPreference.getFoodPref("VEGGIE"), kitchen1, person4);
        person4 = new Person("4", "Diana", AgeGroup.getAgeRange("28"), Gender.genderValue.female, FoodPreference.getFoodPref("NONE"), kitchen2, person3);
        person5 = new Person("5", "Mark", AgeGroup.getAgeRange("40"), Gender.genderValue.male, FoodPreference.getFoodPref("VEGAN"), null, null);
        person6 = new Person("6", "Frank", AgeGroup.getAgeRange("50"), Gender.genderValue.male, FoodPreference.getFoodPref("NONE"), null, null);

        couple1 = new Couple(0,person1, person2, kitchen1, kitchen2, FoodPreference.getFoodPref("VEGAN"), new Location(20.00, 15.00));
        couple2 = new Couple(1,person3, person4, kitchen1, kitchen2, FoodPreference.getFoodPref("VEGGIE"), new Location(20.00, 15.00));

        singles = new ArrayList<>();
        singles.add(person1);
        singles.add(person2);
        singles.add(person3);
        singles.add(person4);
        singles.add(person5);
        singles.add(person6);
    }

    @Test
    public void testCalcCouples() {
        // TODO: Implement this test method once calcCouples is implemented



        coupleManager.givePeopleWithoutPartner(singles, 0, 0, new Location(20.00, 15.00));

        List<Couple> couples = coupleManager.getCouples();

        assertNotNull(couples);
        assertFalse(couples.isEmpty());
        for (Couple couple : couples) {
            assertNotNull(couple.getPerson1());
            assertNotNull(couple.getPerson2());
        }
    }

    @Test
    public void testAddPerson() {
        // TODO: Implement this test method once addPerson is implemented
        coupleManager.addPerson(person1);
        assertTrue(coupleManager.getSingleList().contains(person1));
    }

    @Test
    public void testRemovePerson() {
        // TODO: Implement this test method once removePerson is implemented
        coupleManager.addPerson(person1);
        coupleManager.removeSinglePerson(person1);
        assertFalse(coupleManager.getSingleList().contains(person1));
    }

    @Test
    public void testGetPerson() {
        // TODO: Implement this test method once getPerson is implemented
        coupleManager.addPerson(person1);
        Person retrievedPerson = coupleManager.getSinglePerson(person1.getID());
        assertEquals(person1, retrievedPerson);
    }

    //TODO: Fix the following test
    @Test
    public void testGetCouple() {
        // TODO: Implement this test method once getCouple is implemented
        coupleManager.addPerson(person1);
        coupleManager.addPerson(person2);
        coupleManager.addPerson(person3);
        coupleManager.addPerson(person4);

        coupleManager.givePeopleWithoutPartner(singles, 0,0,new Location(0.2,0.4));
        List<Couple> couples = coupleManager.couples;
        assertNotNull(couples);
    }

    @Test
    public void testCalculateCost() {
        // Same person
        assertEquals(-1, coupleManager.calculateCost(person1, person1));

        // Same kitchen
        assertEquals(-1, coupleManager.calculateCost(person1, person3));

        // Different food preferences: MEAT and VEGAN
        double cost = coupleManager.calculateCost(person1, person2);
        assertTrue(cost > 100);

        // Different food preferences: MEAT and VEGGIE
        cost = coupleManager.calculateCost(person2, person3);
        assertTrue(cost > 80);

        // Different food preferences: VEGAN and VEGGIE
        cost = coupleManager.calculateCost(person5, person3);
        assertTrue(cost > 40);

        // One person has no kitchen
        cost = coupleManager.calculateCost(person1, person6);
        assertTrue(cost >= 0);

        // Both have no kitchen
        assertEquals(-1, coupleManager.calculateCost(person5, person6));

        // Age difference and same gender
        cost = coupleManager.calculateCost(person2, person3);
        assertTrue(cost > 200);

        // Different kitchens and food preferences
        cost = coupleManager.calculateCost(person4, person5);
        assertTrue(cost >= 0);
    }

    @Test
    public void testCancelPerson() {
        // Add debug prints
        //System.out.println("Initial singles: " + coupleManager.getAllSingleParticipants());
        System.out.println("Initial couples: " + coupleManager.getCouples());

        // Add persons
        coupleManager.addPerson(person1);
        coupleManager.addPerson(person2);
        coupleManager.addPerson(person3);
        coupleManager.addPerson(person4);

        // Print state after adding persons
        //System.out.println("Singles after adding persons: " + coupleManager.getAllSingleParticipants());
        System.out.println("Couples after adding persons: " + coupleManager.getCouples());

        // Form initial couples
        coupleManager.givePeopleWithoutPartner(singles, 0, 0, new Location(0.2, 0.4));
        List<Couple> initialCouples = coupleManager.getCouples();
        int initialCouplesSize = initialCouples.size();
        System.out.println("Initial couples after forming: " + initialCouples);

        // Check initial state
        assertNotNull(initialCouples);
        assertFalse(initialCouples.isEmpty());

        // Cancel person1
        coupleManager.cancelPerson(person1);

        // Print state after cancellation
        //System.out.println("Singles after cancelling person1: " + coupleManager.getAllSingleParticipants());
        System.out.println("Couples after cancelling person1: " + coupleManager.getCouples());

        // Get updated couples
        List<Couple> updatedCouples = coupleManager.getCouples();
        System.out.println("Updated couples after cancelling person1: " + updatedCouples);

        // Assert that the couples have changed
        assertEquals(initialCouplesSize - 1, updatedCouples.size());
    }

    @Test
    public void testCancelCouple() {

        // Add debug prints
        System.out.println("Initial singles: " + coupleManager.getAllSingleParticipants());
        System.out.println("Initial couples: " + coupleManager.getCouples());

        // Add persons
        coupleManager.addPerson(person1);
        coupleManager.addPerson(person2);
        coupleManager.addPerson(person3);
        coupleManager.addPerson(person4);

        // Print state after adding persons
        System.out.println("Singles after adding persons: " + coupleManager.getAllSingleParticipants());
        System.out.println("Couples after adding persons: " + coupleManager.getCouples());

        // Form initial couples
        coupleManager.givePeopleWithoutPartner(singles, 0, 0, new Location(0.2, 0.4));
        List<Couple> initialCouples = coupleManager.getCouples();
        int initialCouplesSize = initialCouples.size();
        System.out.println("Initial couples after forming: " + initialCouples);

        // Check initial state
        assertNotNull(initialCouples);
        assertFalse(initialCouples.isEmpty());

        // Cancel couple1
        Couple couple1 = coupleManager.getCouples().get(0);
        coupleManager.cancelCouple(couple1);

        // Print state after cancellation
        System.out.println("Singles after cancelling couple1: " + coupleManager.getAllSingleParticipants());
        System.out.println("Couples after cancelling couple1: " + coupleManager.getCouples());

        // Get updated couples
        List<Couple> updatedCouples = coupleManager.getCouples();
        System.out.println("Updated couples after cancelling couple1: " + updatedCouples);

        // Assert that the couple's size has decreased by one
        assertEquals(initialCouplesSize - 1, updatedCouples.size());

    }

}


