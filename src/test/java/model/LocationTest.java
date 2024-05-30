package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    @Test
    public void testDistance() {
        Location location1 = new Location(0d, 0d);
        Location location2 = new Location(1d, 1d);
        double expectedDistance = 157249; //
        double actualDistance = location1.distance(location2);
        assertEquals(expectedDistance, actualDistance,2);// delta of 2 meters
    }
}