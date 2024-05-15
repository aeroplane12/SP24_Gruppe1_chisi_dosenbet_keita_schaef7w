package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class KitchenTest {
    private static Kitchen kitchen1;
    private static Kitchen kitchen2;

    @BeforeAll
    public static void setUp() {
        // Creating a kitchen instance using the constructor that takes an array of strings
        String[] kitchenInfo1 = {"maybe", "1.5", "40.7128", "-74.0060"};
        kitchen1 = new Kitchen(kitchenInfo1);

        // Creating a kitchen instance using the constructor that takes individual parameters
        double longitude = -77.0369;
        double latitude = 38.9072;
        boolean emergency = true;
        double story = 2.0;
        kitchen2 = new Kitchen(longitude, latitude, emergency, story);
    }

    @Test
    public void testToString() {
        // Check if kitchen1 is not null before accessing its toString() method
        assertNotNull(kitchen1, "Kitchen 1 is null");
        String expectedString1 = "[Kitchen_Status: only for emergencies, Kitchen_Longitude: 40.7128, Kitchen_Latitude: -74.006, Kitchen_Story: 1.5]";
        assertEquals(expectedString1, kitchen1.toString());

        // Check if kitchen2 is not null before accessing its toString() method
        assertNotNull(kitchen2, "Kitchen 2 is null");
        String expectedString2 = "[Kitchen_Status: only for emergencies, Kitchen_Longitude: -77.0369, Kitchen_Latitude: 38.9072, Kitchen_Story: 2.0]";
        assertEquals(expectedString2, kitchen2.toString());
    }

    @Test
    public void testDistance() {
        Location location = new Location(40.7128, -74.0060);
        int expectedDistance = 0; // The distance between the same locations should be 0
        double actualDistance = kitchen1.distance(location);
        assertEquals(expectedDistance, actualDistance);
    }
    @Test
    public void testEquals() {
        Kitchen kitchen3 = new Kitchen(-77.0369, 38.9072, false, 2.0);
        assertNotEquals(kitchen1, kitchen2);
        assertEquals(kitchen3 , kitchen2);
    }
}






