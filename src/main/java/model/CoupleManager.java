package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class CoupleManager {


    private static CoupleManager instance;

    public static CoupleManager getInstance() {
        if (instance == null)
            instance = new CoupleManager();

        return instance;
    }

    List<Couple> couples = new ArrayList<>();
    // might need overview over all People
    List<Person> allParticipants = new ArrayList<>();
    // everyone who is not locked in a left
    List<Person> allSingleParticipants = new ArrayList<>();
    // everyone who is left

    List<Couple> givePeopleWithoutPartner(List<Person> singles) {
        allSingleParticipants.addAll(singles);
        calcCouples();
        return couples;
    }

    void calcCouples() {

        int[][] matrix = new int[allSingleParticipants.size()][allSingleParticipants.size()];

        for (int i = 0; i < allSingleParticipants.size(); i++) {
            for (int j = i; j < allSingleParticipants.size(); j++) {
                matrix[i][j] = calculateCost(allSingleParticipants.get(i), allSingleParticipants.get(j));
            }
        }
    }

    private int calculateCost(Person person1, Person person2) {
        int cost = 0;

        //If food preference is not equal
        if (!person1.getFoodPreference().equals(person2.getFoodPreference())) {
            if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT))
                cost += 100;
            else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT))
                cost += 80;
            else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN))
                cost += 60;
        }

        //If age difference is too big


        return cost;
    }

    public void addPerson(Person person) {
        if (person.hasPartner()) {
            allParticipants.add(person);
            allParticipants.add(person.getPartner());
        } else {
            allParticipants.add(person);
            allSingleParticipants.add(person);
            calcCouples();
        }
    }

    public void removePerson(String personID) {
        Person person = getPerson(personID);
        allParticipants.remove(person);
        allSingleParticipants.remove(person);
        calcCouples();
    }

    public void removeCouple(String CoupleID) {
        allParticipants = allParticipants.stream()
                .filter(x -> !x.getCoupleIDs().equals(CoupleID))
                .toList();
    }

    public Person getPerson(String string) {
        return allParticipants.stream()
                .filter(x -> x.getID().equals(string))
                .findAny()
                .orElse(null);
    }

    /**
     * getCouple()
     * returns a Couple by their CoupleID
     *
     * @param string the CoupleID
     * @return the Couple
     */
    public Person[] getCouple(String string) {
        return allParticipants.stream()
                .map(Person::getCouple)
                .filter(Objects::nonNull)
                .filter(x -> x[0].getCoupleIDs().equals(string))
                .findFirst()
                .orElse(null);
    }
}
