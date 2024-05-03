package model;

import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;


import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    Manager manger;

    @BeforeClass
    void setUp(){
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        this.manger = new Manager(groupManager, coupleManager);
    }

    @Test
    public void testCoupleManager() {
        CoupleManager coupleManager = manger.getCoupleManager();
        AgeGroup.AgeRange age = AgeGroup.AgeRange.AGE_31_35;
        Gender.genderValue gender = Gender.genderValue.male;
        FoodPreference.FoodPref foodPref = FoodPreference.FoodPref.MEAT;
        Kitchen kitchen = new Kitchen(15.00,34.00,true,3.0);

        Person partner = new Person("2","alice",age, gender,foodPref,kitchen,null);
        Person testPerson = new Person("1","Bob",age, gender,foodPref,kitchen,partner);
        partner.setPartner(testPerson);

        coupleManager.addPerson(testPerson);

        //As person has partner both should be added
        assertEquals(coupleManager.allParticipants.length, 2);

    }


}