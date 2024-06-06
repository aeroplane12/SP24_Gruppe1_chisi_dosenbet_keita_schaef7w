package model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CoupleManager {
    // might need overview over all People
    List<Person> allParticipants = new ArrayList<>();
    // everyone who is not locked in left
    List<Person> allSingleParticipants = new ArrayList<>();
    // everyone who is left
    private final List<RatingPriority> ratingPriorityOrder = new ArrayList<>(3);

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
        int foodPrefWeight;
        int ageDiffWeight;
        int genderDiffWeight;
        // default ranking order for the rating system
        if (ratingPriorityOrder.isEmpty()) {
            ratingPriorityOrder.add(RatingPriority.GENDERDIFFERENCE);
            ratingPriorityOrder.add(RatingPriority.AGEDIFFERENCE);
            ratingPriorityOrder.add(RatingPriority.FOODPREFERENCE);
            foodPrefWeight = 100;
            ageDiffWeight = 10;
            genderDiffWeight = 1;
        } else {
            foodPrefWeight = (int) Math.pow(10, ratingPriorityOrder.indexOf(RatingPriority.FOODPREFERENCE));
            ageDiffWeight = (int) Math.pow(10, ratingPriorityOrder.indexOf(RatingPriority.AGEDIFFERENCE));
            genderDiffWeight = (int) Math.pow(10, ratingPriorityOrder.indexOf(RatingPriority.GENDERDIFFERENCE));
        }


        int[][] coupleRating = new int[allSingleParticipants.size()][allSingleParticipants.size()];
        for (int i = 0; i < coupleRating.length; i++) {
            for (int j = i + 1; j < coupleRating.length; j++) {
                coupleRating[i][j] = calculateCoupleRating(allParticipants.get(i), allParticipants.get(j), foodPrefWeight, ageDiffWeight, genderDiffWeight);
            }
        }

    }
    private int calculateCoupleRating(Person person1, Person person2, int foodPrefWeight, int ageDiffWeight, int genderDiffWeight){
        // are the FoodPreferences compatible
        if ((person1.getFoodPreference().value == 1 && person2.getFoodPreference().value > 1) ||
            person2.getFoodPreference().value == 1 && person1.getFoodPreference().value > 1) {
            return 1000;
        }
        // does at least one person have a kitchen
        if (person1.getKitchen() == null && person2.getKitchen() == null) {
            return 1000;
        }
        // are they living in the same building
        if (person1.getKitchen().distance(person2.getKitchen()) == 0) {
            return 1000;
        }
        int rating = 0;
        // if egali and vegi/vegan are tested, half the rating, prefer egali and meat
        if ((person1.getFoodPreference().value == 0 && person2.getFoodPreference().value > 1) ||
            person2.getFoodPreference().value == 0 && person1.getFoodPreference().value > 1) {
            rating = foodPrefWeight;
        }
        // computes a rating for the age difference,
        rating += ageDiffWeight * Math.abs(person1.getAge().value - person2.getAge().value) ;
        // adjusting rating based on the gender of both ppl
        if (person1.getGender() == person2.getGender()) {
            rating += genderDiffWeight;
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

    public List<Couple> givePeopleWithoutPartner(List<Person> singles) {
        return null;
    }
}
