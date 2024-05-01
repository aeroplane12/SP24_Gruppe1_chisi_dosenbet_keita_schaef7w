package model;


import java.util.Arrays;

public class Person {
    private final String ID;
    private String name;
    private FoodPreference.FoodPref foodPreference;
    private AgeGroup.AgeRange age;
    private Gender.genderValue gender;
    private Kitchen kitchen;
    public Person(String[] strings){
        ID = strings[1];
        name = strings[2];
        foodPreference = FoodPreference.getFoodPref(strings[3]);
        age = AgeGroup.getAgeRange(strings[4]);
        gender = Gender.getGen(strings[5]);
        kitchen = strings[6].equals("no") || strings[6].isEmpty() ? null : new Kitchen(Arrays.copyOfRange(strings,6,10));
    }
    public Person(String[] strings, FoodPreference.FoodPref food, Kitchen k){
        ID = strings[0];
        name = strings[1];
        age = AgeGroup.getAgeRange(strings[2]);
        gender = Gender.getGen(strings[3]);
        foodPreference = food;
        kitchen = k;
    }


    public FoodPreference.FoodPref getFoodPreference() {
        return foodPreference;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    @Override
    public String toString() {
        return "[PersonID: " + ID +
                ", PersonName: " + name +
                ", FoodPreference: " + foodPreference +
                ", Age: " + age +
                ", Gender: " + gender +
                ", Kitchen" + kitchen.toString() +
                "]";

    }
}
