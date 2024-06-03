package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CoupleManager {
    // might need overview over all People
    List<Person> allParticipants = new ArrayList<>();
    // everyone who is not locked in left
    List<Person> allSingleParticipants = new ArrayList<>();
    // everyone who is left

    void calcCouples(){
        //TODO Algorithm to sort people into couples
        // needs to be in accordance with the specifications:
        // - Strictly according to their FoodPreference,
        // meaning there should be no couples who dont eat the same food
        // - Sorted according to their Age Group
        // - no couple without a kitchen, no couple with the same kitchen

        /*TODO
            PFLICHT
            - Esssensvorliebe :
            Fleisch + Fleisch = Fleisch,
            Fleisch + Egal = Fleisch,
            Fleisch/Egal + Vegetarisch/Vegan = Vegetarisch/Vegan,
            Vegan + Vegetarisch = Vegan,
            Fleisch darf nicht mit Vegan/Vegetarisch gepaart werden (außer sie melden sich zusammen an)
            - Die Küche wählen, welche am nächsten an der After-Dinner-Party ist, Pärchen ohne Küche ungültig
            - Küchen dürfen nicht mehr als 3 mal als Paar besetzt werden
            - Teilnehmer dürfen nur dann Pärchen bilden, wenn distanz zwischen ihnen nicht 0 ist
            RANKING
            - Möglichst identische Essensvorliebe
            - Möglichst kleiner altersunterschied bei Pärchen
            - Geschlechter sollten möglichst durchmischt sein
            - Weglänge der Pärchen sollte möglichst klein sein, Laufrichtung sollte am besten in Richtung
            Afterparty gehen. Man könnte die Quartile berechnen und somit die Locations ausmachen, welche sich
            am besten für Vorspeise, Hauptspeise und Desert am besten eignen???
            - Kann sein, dass Personen keinen Partnern zugeordnet werden können, weshalb diese mit Nachrückern zusammengebracht werden sollen
        */
        allSingleParticipants = allParticipants.stream().filter(person -> !person.hasPartner()).toList();
        List<Person> fleshPrefGroup = allSingleParticipants.stream()
                .filter(person -> person.getFoodPreference() == FoodPreference.FoodPref.MEAT ||
                        person.getFoodPreference() == FoodPreference.FoodPref.NONE)
                .toList();
        List<Person> vegiGroup = allSingleParticipants.stream()
                .filter(person -> !(person.getFoodPreference() == FoodPreference.FoodPref.MEAT))
                .toList();

        double[][] coupleRating = new double[allSingleParticipants.size()][allSingleParticipants.size()];
        for (int i = 0; i < coupleRating.length; i++) {
            for (int j = i + 1; j < coupleRating.length; j++) {

            }
        }
    }
    private double calculateCoupleRating(Person person1, Person person2){
        // are the FoodPreferences compatible
        if ((person1.getFoodPreference().value == 1 && person2.getFoodPreference().value > 1) ||
            person2.getFoodPreference().value == 1 && person1.getFoodPreference().value > 1) {
            return 0;
        }
        // does at least one person have a kitchen
        if (person1.getKitchen() == null && person2.getKitchen() == null) {
            return 0;
        }
        // are they living in the same building
        if (person1.getKitchen().distance(person2.getKitchen()) == 0) {
            return 0;
        }
        double rating = 1;
        // if egali and vegi/vegan are tested, half the rating, prefer egali and meat
        // 0.5 is a placeholder for now
        if ((person1.getFoodPreference().value == 0 && person2.getFoodPreference().value > 1) ||
            person2.getFoodPreference().value == 0 && person1.getFoodPreference().value > 1) {
            rating = 0.5;
        }
        // computes a rating for the age difference,
        // 0.1 is a placeholder for now
        rating *= 1 - 0.1 * Math.abs(person1.getAge().value - person2.getAge().value) ;
        // adjusting rating based on the gender of both ppl
        // 0.7 another magic number
        if (person1.getGender() == person2.getGender()) {
            rating *= 0.7;
        }

        return rating;

    }
    public void addPerson(Person person){
        if (person.hasPartner()) {
            allParticipants.add(person);
            allParticipants.add(person.getPartner());
        } else {
            allParticipants.add(person);
            allSingleParticipants.add(person);
            calcCouples();
        }
    }

    public void removePerson(String personID){
        Person person = getPerson(personID);
        allParticipants.remove(person);
        allSingleParticipants.remove(person);
        calcCouples();
    }
    public void removeCouple(String CoupleID){
        allParticipants = allParticipants.stream()
                .filter(x->!x.getCoupleIDs().equals(CoupleID))
                .toList();
    }
    public Person getPerson(String string){
        return allParticipants.stream()
                .filter(x->x.getID().equals(string))
                .findAny()
                .orElse(null);
    }

    /** getCouple()
     * returns a Couple by their CoupleID
     * @param string the CoupleID
     * @return the Couple
     */
    public Person[] getCouple(String string){
        return allParticipants.stream()
                .map(Person::getCouple)
                .filter(Objects::nonNull)
                .filter(x->x[0].getCoupleIDs().equals(string))
                .findFirst()
                .orElse(null);
    }
}
