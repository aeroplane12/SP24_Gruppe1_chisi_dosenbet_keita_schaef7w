package model;

import model.tools.CSVReader;
import model.tools.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.*;

public class GroupManagerTest {
    private GroupManager groupManager;
    int i = 0;
    Location partyLoc;
    List<Couple> couples;

    @BeforeEach
    public void setUp() {
        partyLoc = new Location(10.0, 20.0);
        groupManager = new GroupManager();
        groupManager.partyLoc = partyLoc;
        couples = CSVReader.csvReaderPeople("Dokumentation/TestingData/teilnehmerliste.csv").stream().filter(Person::hasPartner).map(x->new Couple(i++ ,
                x,
                x.getPartner(),
                x.getKitchen(),
                x.getPartner().getKitchen(),
                x.getCouplePreference(),
                partyLoc)).toList();
    }

    @Test
    public void testCalcGroups() {
        // TODO: Implement this test method once calcGroups is implemented
        groupManager.calcGroups(couples);
        Map<Course, List<Group>> courseGroupMap = GroupManager.getLedger().stream().collect(groupingBy(Group::getCourse));
        Map<Couple, Integer> coupleCounter = new HashMap<>();
        Map<Kitchen, Integer> kitchenCounter = new HashMap<>();
        int mapEntrySize = courseGroupMap.values().stream().toList().get(0).size();
        for (List<Group> i : courseGroupMap.values()) {
            assertEquals(mapEntrySize,i.size(),"differently sized Group allocations");
            for (Group j : i){
                for (Couple h : j.getAll()) {
                    if (!coupleCounter.containsKey(h)){
                        coupleCounter.put(h,1);
                    } else {
                        coupleCounter.put(h,coupleCounter.get(h)+1);
                    }
                    assertTrue(h.WasHost(),"every Couple needs to have been host at exactly once");
                    for (int g:h.getWithWhomAmIEating().values()) {
                        assertNotEquals(g,-1,"every Couple has to have the groupID registered in WithWhomAmIEating");
                    }
                    if (!kitchenCounter.containsKey(h.getCurrentKitchen())) {
                        kitchenCounter.put(h.getCurrentKitchen(),1);
                    } else {
                        kitchenCounter.put(h.getCurrentKitchen(),kitchenCounter.get(h.getCurrentKitchen())+1);
                    }
                }
                //test assertions for groups here
            }
        }
        for (Integer i : coupleCounter.values()) {
            assertEquals(i,3,"every Couple needs to be in 3 groups");
        }
        //this is also testing resolvingKitchenConflicts()
        for (Integer i : coupleCounter.values()) {
            assertEquals(i,3,"a kitchen cant be used more than 3 times");
        }
        // EDGE CASES NULL/EMPTY-LIST
        GroupManager.clear();
        groupManager.calcGroups(Collections.emptyList());
        List<Group> groups1 = GroupManager.getLedger();
        assertNotNull(groups1);
        assertTrue(groups1.isEmpty(), "No groups should be generated for empty input");
        // EDGE CASE LESS THAN 9
        groupManager.calcGroups(List.of(couples.get(0)));
        List<Group> groups2 = GroupManager.getLedger();
        assertNotNull(groups2);
        assertTrue(groups2.isEmpty(), "No groups should be generated when couples are less than 9 couples");
    }

    @Test
    public void testKitchen() {
        /*
        Kitchen kitchen1 = new Kitchen(40.7128, -74.0060, false, 1.0);
        Kitchen kitchen2 = new Kitchen(34.0522, -118.2437, false, 1.5);
        Kitchen kitchen3 = new Kitchen(51.5074, -0.1278, false, 1.2);
        // Test kitchenConflicts method
        for (Group i: GroupManager.getLedger()) {
            i.getHosts().getCurrentKitchen();
        }

        assertEquals(0, conflictingCouples1.size(), "No conflicts should be detected in kitchen 1");
        assertEquals(0, conflictingCouples2.size(), "No conflicts should be detected in kitchen 2");
        assertEquals(0, conflictingCouples3.size(), "No conflicts should be detected in kitchen 3");

        // Test resolveKitchenConflicts method

        Couple couple3 = new Couple(3, person1, person2, null, null, FoodPreference.FoodPref.MEAT, null);
        Couple couple4 = new Couple(4, person3, person4, null, null, FoodPreference.FoodPref.MEAT, null);
        Couple couple5 = new Couple(5, person1, person2, null, null, FoodPreference.FoodPref.MEAT, null);
        List<Couple> additionalCouples = Arrays.asList(couple3, couple4, couple5);
        groupManager.allCouples.addAll(additionalCouples);

        groupManager.resolveKitchenConflicts();

        // Check if couples are moved to overBookedCouples list if conflicts cannot be resolved
        assertTrue(groupManager.overBookedCouples.contains(couple3), "Couple 3 should be in overBookedCouples list");
        assertTrue(groupManager.overBookedCouples.contains(couple4), "Couple 4 should be in overBookedCouples list");
        assertTrue(groupManager.overBookedCouples.contains(couple5), "Couple 5 should be in overBookedCouples list");

        // Check if couples are moved to succeedingCouples list if conflicts cannot be resolved and couples exceed limit
        assertTrue(groupManager.succeedingCouples.contains(couple3), "Couple 3 should be in succeedingCouples list");
        assertTrue(groupManager.succeedingCouples.contains(couple4), "Couple 4 should be in succeedingCouples list");
        assertTrue(groupManager.succeedingCouples.contains(couple5), "Couple 5 should be in succeedingCouples list");

        // Check if couples are properly moved between kitchens if conflicts are resolved
        assertFalse(groupManager.overBookedCouples.contains(couple1), "Couple 1 should not be in overBookedCouples list");
        assertFalse(groupManager.overBookedCouples.contains(couple2), "Couple 2 should not be in overBookedCouples list");
        assertFalse(groupManager.succeedingCouples.contains(couple1), "Couple 1 should not be in succeedingCouples list");
        assertFalse(groupManager.succeedingCouples.contains(couple2), "Couple 2 should not be in succeedingCouples list");
         */
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

        List<Group> groups = groupManager.fillCourse(hostCouples, Course.STARTER);

        assertNotNull(groups);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertNotNull(group);
        assertEquals(3, group.getAll().size());
    }

    @Test
    public void testFindGuests() {
        Kitchen kitchen1 = new Kitchen(40.7128, -74.0060, false, 1.0); // Assume a single kitchen for simplicity
        Kitchen kitchen2 = new Kitchen(40.7129, -74.0061, false, 1.0); // Assume a single kitchen for simplicity
        Couple hostCouple = new Couple(1, new Person("1", "Alice", AgeGroup.AgeRange.AGE_18_23, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, kitchen1, null),
                new Person("2", "Bob", AgeGroup.AgeRange.AGE_28_30, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), kitchen1, null, FoodPreference.FoodPref.MEAT, null);
        Couple guestCouple1 = new Couple(2, new Person("3", "Charlie", AgeGroup.AgeRange.AGE_36_41, Gender.genderValue.other, FoodPreference.FoodPref.VEGGIE, null, null),
                new Person("4", "David", AgeGroup.AgeRange.AGE_47_56, Gender.genderValue.male, FoodPreference.FoodPref.NONE, null, null), kitchen2, null, FoodPreference.FoodPref.NONE, null);
        Couple guestCouple2 = new Couple(3, new Person("5", "Eva", AgeGroup.AgeRange.AGE_24_27, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null),
                new Person("6", "Frank", AgeGroup.AgeRange.AGE_31_35, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), kitchen2, null, FoodPreference.FoodPref.VEGAN, null);

        List<Couple> possibleGuests = Arrays.asList(guestCouple1, guestCouple2);

        Course course = Course.STARTER;
        Group group = groupManager.findGuests(hostCouple, possibleGuests, course);

        assertNotNull(group);
        assertEquals(3, group.getAll().size(), "The group should have 3 couples");
        assertTrue(group.getAll().contains(hostCouple), "The group should contain the host couple");
        assertTrue(group.getAll().contains(guestCouple1), "The group should contain the first guest couple");
        assertTrue(group.getAll().contains(guestCouple2), "The group should contain the second guest couple");
    }

}