package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person("ID", "Name", AgeGroup.AgeRange.AGE_0_17, Gender.genderValue.male, FoodPreference.FoodPref.MEAT, new Kitchen(15.0, 20.0, true, 30.0), null);
    }

    @Test
    public void testGetFoodPreference() {
        assertEquals(FoodPreference.FoodPref.MEAT, person.getFoodPreference());
    }

    @Test
    public void testGetKitchen() {
        // TODO: Implement this test method once you know what to expect from getKitchen
    }

    @Test
    public void testGetName() {
        assertEquals("Name", person.getName());
    }

    @Test
    public void testGetID() {
        assertEquals("ID", person.getID());
    }

    @Test
    public void testGetPartner() {
        assertNull(person.getPartner());
    }

    @Test
    public void testHasPartner() {
        assertFalse(person.hasPartner());
    }

    @Test
    public void testGetCouple() {
        assertNull(person.getCouple());
    }

    @Test
    public void testGetCoupleIDs() {
        assertNull(person.getCoupleIDs());
    }
}