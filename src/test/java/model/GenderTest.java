package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenderTest {

    @Test
    public void testGetGen_Male() {
        String genderString = "male";
        Gender.genderValue expected = Gender.genderValue.male;
        Gender.genderValue actual = Gender.getGen(genderString);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGen_Female() {
        String genderString = "female";
        Gender.genderValue expected = Gender.genderValue.female;
        Gender.genderValue actual = Gender.getGen(genderString);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGen_Other() {
        String genderString = "other";
        Gender.genderValue expected = Gender.genderValue.other;
        Gender.genderValue actual = Gender.getGen(genderString);
        assertEquals(expected, actual);
    }
}