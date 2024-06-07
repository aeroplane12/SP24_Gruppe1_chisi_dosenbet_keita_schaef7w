package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GroupManagerTest {
    private GroupManager groupManager;
    private Couple couple1;
    private Couple couple2;
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;

    @BeforeEach
    public void setUp() {
        groupManager = new GroupManager();

        person1 = new Person("1", "Alice", AgeGroup.AgeRange.AGE_18_23, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null);
        person2 = new Person("2", "Bob", AgeGroup.AgeRange.AGE_28_30, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null);
        person3 = new Person("3", "Charlie", AgeGroup.AgeRange.AGE_36_41, Gender.genderValue.other, FoodPreference.FoodPref.VEGGIE, null, null);
        person4 = new Person("4", "David", AgeGroup.AgeRange.AGE_47_56, Gender.genderValue.male, FoodPreference.FoodPref.NONE, null, null);

        couple1 = new Couple(1, person1, person2, null, null, FoodPreference.FoodPref.MEAT, null);
        couple2 = new Couple(2, person3, person4, null, null, FoodPreference.FoodPref.VEGAN, null);
    }

    @Test
    public void testCalcGroups() {
        // TODO: Implement this test method once calcGroups is implemented
        List<Couple> couples = Arrays.asList(couple1, couple2);
        groupManager.calcGroups(couples);
        Map<Course, List<Group>> ledger = groupManager.ledger;

        for (Course course : Course.values()) {
            List<Group> groups = ledger.get(course);
            assertNotNull(groups);
            assertEquals(3, groups.size(), "There should be 3 groups for each course");

            for (Group group : groups) {
                assertNotNull(group);
                assertEquals(3, group.getAll().size(), "Each group should have 3 couples");
            }
        }
    }

    @Test
    public void testKitchen() {
        Kitchen kitchen1 = new Kitchen(40.7128, -74.0060, false, 1.0);
        Kitchen kitchen2 = new Kitchen(34.0522, -118.2437, false, 1.5);
        Kitchen kitchen3 = new Kitchen(51.5074, -0.1278, false, 1.2);

        List<Couple> couples = Arrays.asList(couple1, couple2);
        groupManager.allCouples = couples;
        List<Couple> conflictingCouples = groupManager.kitchenConflicts(kitchen3);

        assertEquals(0, conflictingCouples.size());
        assertFalse(conflictingCouples.contains(couple2));
    }

    @Test
    public void testFillCourse() {
        Couple hostCouple1 = new Couple(5, new Person("5", "Eva", AgeGroup.AgeRange.AGE_24_27, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null),
                new Person("6", "Frank", AgeGroup.AgeRange.AGE_31_35, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), null, null, FoodPreference.FoodPref.MEAT, null);
        Couple hostCouple2 = new Couple(6, new Person("7", "Grace", AgeGroup.AgeRange.AGE_42_46, Gender.genderValue.other, FoodPreference.FoodPref.VEGGIE, null, null),
                new Person("8", "Henry", AgeGroup.AgeRange.AGE_57_Infin, Gender.genderValue.male, FoodPreference.FoodPref.NONE, null, null), null, null, FoodPreference.FoodPref.NONE, null);
        Couple hostCouple3 = new Couple(7, new Person("9", "Ivy", AgeGroup.AgeRange.AGE_18_23, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null),
                new Person("10", "Jack", AgeGroup.AgeRange.AGE_24_27, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), null, null, FoodPreference.FoodPref.VEGAN, null);

        List<Couple> hostCouples = Arrays.asList(hostCouple1, hostCouple2, hostCouple3);

        Kitchen kitchen = new Kitchen(40.7128, -74.0060, false, 1.0); // Assume a single kitchen for simplicity
        for (Couple hostCouple : hostCouples) {
            hostCouple.setKitchen1(kitchen);
        }

        GroupManager groupManager = new GroupManager();
        List<Group> groups = groupManager.fillCourse(hostCouples, Course.STARTER);

        assertNotNull(groups);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertNotNull(group);
        assertEquals(3, group.getAll().size());
    }

    @Test
    public void testFindGuests() {
        couple1.meetsCouple(couple2);
        couple2.meetsCouple(couple1);

        Kitchen kitchen = new Kitchen(40.7128, -74.0060, false, 1.0); // Assume a single kitchen for simplicity

        List<Couple> couples = Arrays.asList(couple1, couple2);

        for (Couple couple : couples) {
            couple.setKitchen1(kitchen);
        }

        GroupManager groupManager = new GroupManager();
        List<Group> groups = groupManager.fillCourse(couples, Course.STARTER);

        assertNotNull(groups);
        assertEquals(1, groups.size());

        Group group = groups.get(0);

        assertNotNull(group);
        assertEquals(2, group.getAll().size());
    }

}