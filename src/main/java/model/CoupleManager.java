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
            for (int j = 0; j < allSingleParticipants.size(); j++) {
                if (i == j)
                    matrix[i][j] = -1;
                else
                    matrix[i][j] = calculateCost(allSingleParticipants.get(i), allSingleParticipants.get(j));
            }
        }

        // Subtract the smallest row value from all values in that row
        for (int i = 0; i < matrix.length; i++) {
            int firstRowVal = matrix[i][0];
            for (int j = 1; j < matrix[i].length; j++) {
                // Find the smallest value in the row and making sure it is not -1 (error value)
                if (matrix[i][j] < firstRowVal && matrix[i][j] != -1) {
                    firstRowVal = matrix[i][j];
                }
            }

            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] -= firstRowVal;
            }
        }
    }


    private int calculateCost(Person person1, Person person2) {
        int cost = 0;

        //Can#t have singles build a pair if they are in the same building
        // or have the same kitchen and are not registered as a couple
        if (person1.getKitchen().distance(person2.getKitchen()) == 0)
            return -1;

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
                cost += 40;
        }

        // If both have a kitchen distance is the added cost if one doesn't and the other does the cost is reduced
        if (person1.getKitchen() != null && person2.getKitchen() != null)
            cost += (int) Math.round(person1.getKitchen().distance(person2.getKitchen()));
        else if (cost >= 30 && (person1.getKitchen() == null || person2.getKitchen() == null))
            cost -= 30;

        //If age difference is too big
        cost += Math.abs(person1.getAge().ordinal() - person2.getAge().ordinal()) * 10;

        if (person1.getGender().equals(person2.getGender()))
            cost += 50;

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
