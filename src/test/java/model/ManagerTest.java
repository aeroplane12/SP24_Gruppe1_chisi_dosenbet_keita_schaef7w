package model;

import org.testng.annotations.BeforeClass;


import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @BeforeClass
    void setUp(){
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        Manager Manager = new Manager(groupManager, coupleManager);

    }


}