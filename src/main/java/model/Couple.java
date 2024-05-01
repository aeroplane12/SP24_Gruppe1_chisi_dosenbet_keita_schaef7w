package model;

import java.util.Arrays;

public class Couple {
    Person[] people = new Person[2];
    Couple(String[] strings){
        //TODO creating CoupleIDs
        people[0] = new Person(strings);
        String[] s = Arrays.copyOfRange(strings, 10, 14);
        people[1] = new Person(s, people[0].getFoodPreference(), people[0].getKitchen());
    }

}
