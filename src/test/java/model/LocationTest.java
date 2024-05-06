package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    @Test
    public void testDistance() {
        Location location1 = new Location(0, 0);
        Location location2 = new Location(3, 4);
        int expectedDistance = 5; // The distance between (0,0) and (3,4) is 5 units.
        int actualDistance = location1.distance(location2);
        assertEquals(expectedDistance, actualDistance);
    }
}