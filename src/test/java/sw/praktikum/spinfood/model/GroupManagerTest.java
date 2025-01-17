package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.CSVReader;
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
        GroupManager.getInstance().clear();
        Manager.maxGuests = 400;
        Manager.getInstance().partyLoc = new Location(10.0, 20.0);
        groupManager = new GroupManager();
        // the entries of teilnehmerliste * 5 so we can guarantee kitchen Conflicts
        couples = new ArrayList<>(Objects.requireNonNull(CSVReader.csvReaderPeople("Dokumentation/TestingData/csvtestdateMult.csv")).stream()
                .filter(Person::hasPartner)
                .map(x->new Couple(i++ ,
                        x,
                        x.getPartner(),
                        x.getKitchen(),
                        x.getPartner().getKitchen(),
                        x.getCouplePreference(),
                        partyLoc)).toList());
        groupManager.calcGroups(new ArrayList<>(couples),false);
    }

    @Test
    public void testCalcGroups() {
        Map<Course, List<Group>> courseGroupMap = groupManager.getLedger().stream().collect(groupingBy(Group::getCourse));
        Map<Couple, Integer> coupleCounter = new HashMap<>();
        assertFalse(GroupManager.getInstance().getLedger().isEmpty());
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
                    assertTrue(h.wasHost(),"every Couple needs to have been host at exactly once");
                    assertEquals(h.getMetCouples().size(),7,"every Couple needs to have met exactly 6 other Couples and themselves");
                    for (int g:h.getWithWhomAmIEating().values()) {
                        assertNotEquals(-1,g,"every Couple has to have the groupID registered in WithWhomAmIEating");
                    }
                }
                //test assertions for groups here
            }
        }
        for (Integer i : coupleCounter.values()) {
            assertEquals(i,3,"every Couple needs to be in exactly 3 groups");
        }
        //test for size
        assertEquals(groupManager.processedCouples.size(),groupManager.getLedger().size());
        // EDGE CASES NULL/EMPTY-LIST
        GroupManager.getInstance().clear();
        groupManager.calcGroups(Collections.emptyList(),false);
        List<Group> groups1 = groupManager.getLedger();
        assertNotNull(groups1);
        assertTrue(groups1.isEmpty(), "No groups should be generated for empty input");
        // EDGE CASE LESS THAN 9
        groupManager.calcGroups(List.of(couples.get(0)),false);
        List<Group> groups2 = groupManager.getLedger();
        assertNotNull(groups2);
        assertTrue(groups2.isEmpty(), "No groups should be generated when couples are less than 9 couples");
    }

    @Test
    public void testKitchen() {
        Map<Course, List<Group>> courseGroupMap = groupManager.getLedger().stream().collect(groupingBy(Group::getCourse));
        Map<Kitchen, Integer> kitchenCounter = new HashMap<>();
        for (List<Group> i : courseGroupMap.values()) {
            for (Group j : i){
                Kitchen usedKitchen = j.getHosts().getCurrentKitchen();
                if (!kitchenCounter.containsKey(usedKitchen)) {
                    kitchenCounter.put(usedKitchen,1);
                } else {
                    kitchenCounter.put(usedKitchen,kitchenCounter.get(usedKitchen)+1);
                }
            }
        }
        for (Integer i : kitchenCounter.values()) {
            assertTrue(3 >= i,"a kitchen cant be used more than 3 times");
        }
        /*
        // sorry keita aber die methoden haben sich mit der Zeit ziemlich verändert
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
        GroupManager.getInstance().clear();
        /*
        Couple hostCouple1 = new Couple(5,
                new Person("5", "Eva", AgeGroup.AgeRange.AGE_24_27, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null),
                new Person("6", "Frank", AgeGroup.AgeRange.AGE_31_35, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), null, null, FoodPreference.FoodPref.MEAT, null);
        Couple hostCouple2 = new Couple(6,
                new Person("7", "Grace", AgeGroup.AgeRange.AGE_42_46, Gender.genderValue.other, FoodPreference.FoodPref.VEGGIE, null, null),
                new Person("8", "Henry", AgeGroup.AgeRange.AGE_57_Infin, Gender.genderValue.male, FoodPreference.FoodPref.NONE, null, null), null, null, FoodPreference.FoodPref.NONE, null);
        Couple hostCouple3 = new Couple(7,
                new Person("9", "Ivy", AgeGroup.AgeRange.AGE_18_23, Gender.genderValue.female, FoodPreference.FoodPref.MEAT, null, null),
                new Person("10", "Jack", AgeGroup.AgeRange.AGE_24_27, Gender.genderValue.male, FoodPreference.FoodPref.VEGAN, null, null), null, null, FoodPreference.FoodPref.VEGAN, null);

        List<Couple> hostCouples = Arrays.asList(hostCouple1, hostCouple2, hostCouple3);

        Kitchen kitchen = new Kitchen(40.7128, -74.0060, false, 1.0); // Assume a single kitchen for simplicity
        for (Couple hostCouple : hostCouples) {
            hostCouple.setKitchen1(kitchen);
        }

        List<Group> groups = groupManager.calcGroups(hostCouples, );

        assertNotNull(groups);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertNotNull(group);
        assertEquals(3, group.getAll().size());
         */
    }


    @Test
    public void testRemoveCouple() {
        Person person3 = new Person("2,01be5c1f-4aa1-458d-a530-b1c109ffbb55,Person3,vegan,22,male,yes,,8.681372017093311,50.5820794170933,117ee996-14d3-44e8-8bcb-eb2d29fddda5,Personx1,25.0,male".split(","));
        Person personx1 = person3.getPartner();
        Couple unkownCouple = new Couple(i,person3,personx1,person3.getKitchen(),personx1.getKitchen(),person3.getCouplePreference(),partyLoc);
        List<Couple> toDelete = new ArrayList<>(groupManager.processedCouples);
        List<Group> l = groupManager.getLedger();
        // nothing should happen
        int before =groupManager.succeedingCouples.size();
        groupManager.remove(unkownCouple);
        assertEquals(before,groupManager.succeedingCouples.size());
        // removing couples that are in overbooked and in succeeding lists
        before = groupManager.succeedingCouples.size();
        groupManager.remove(groupManager.succeedingCouples.get(0));
        assertEquals(before-1,groupManager.succeedingCouples.size());
        before = groupManager.overBookedCouples.size();
        if (before != 0) {
            groupManager.remove(groupManager.overBookedCouples.get(0));
            assertEquals(before-1,groupManager.overBookedCouples.size());
        }
        assertEquals(groupManager.getLedger(),l);
        for (Couple couple : toDelete) {
            //remove from manager, commented out due to performance concerns
            groupManager.remove(couple);
            assertFalse(groupManager.processedCouples.contains(couple));
            if (groupManager.ledger.isEmpty()) {
                break;
            }
        }
        // checking integrity of all things again
        testCalcGroups();
    }

    @Test
    public void testForceGroups(){
        int size  = groupManager.succeedingCouples.size();
        groupManager.forceGroups(groupManager.succeedingCouples);
        assertTrue(size >= groupManager.succeedingCouples.size());
        // checking for integrity, don't know if that works
        testCalcGroups();

    }
    @Test
    public void testClear(){
        groupManager.clear();
        assertTrue(groupManager.ledger.isEmpty());
        assertTrue(groupManager.processedCouples.isEmpty());
        assertTrue(groupManager.getKitchenLedger().isEmpty());

    }
}