package sw.praktikum.spinfood.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgeGroupTest {

    @Test
    public void testGetAgeRange_0_17() {
        String age = "17";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_0_17;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_18_23() {
        String age = "23";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_18_23;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_24_27() {
        String age = "27";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_24_27;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_28_30() {
        String age = "30";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_28_30;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_31_35() {
        String age = "35";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_31_35;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_36_41() {
        String age = "41";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_36_41;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_42_46() {
        String age = "46";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_42_46;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_47_56() {
        String age = "56";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_47_56;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAgeRange_57_Infin() {
        String age = "57";
        AgeGroup.AgeRange expected = AgeGroup.AgeRange.AGE_57_Infin;
        AgeGroup.AgeRange actual = AgeGroup.getAgeRange(age);
        assertEquals(expected, actual);
    }
}