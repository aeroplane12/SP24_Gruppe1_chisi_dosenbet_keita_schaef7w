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

        Kitchen sameKitchen = new Kitchen(15.0, 20.0, true, 30.0);
        Kitchen diffKitchen = new Kitchen(15.0, 22.0, true, 30.0);
        assertEquals(sameKitchen, person.getKitchen());
        assertEquals(sameKitchen.isEmergency(), person.getKitchen().isEmergency());
        assertNotEquals(diffKitchen, person.getKitchen());
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