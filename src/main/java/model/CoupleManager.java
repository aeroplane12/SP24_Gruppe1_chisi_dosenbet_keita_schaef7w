package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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

        // Group single participants by their food preference
        Map<FoodPreference.FoodPref, List<Person>> groupedByFood = allSingleParticipants.stream()
                .collect(Collectors.groupingBy(Person::getFoodPreference));

        List<Person> newCouples = new ArrayList<>();

        // Iterate over each food preference group
        for (List<Person> foodGroup : groupedByFood.values()) {
            // Sort by age group within each food preference group
            Map<AgeGroup.AgeRange, List<Person>> groupedByAge = foodGroup.stream()
                    .collect(Collectors.groupingBy(Person::getAgeGroup));

            // For each age group, form couples
            for (List<Person> ageGroup : groupedByAge.values()) {
                int i = 0;
                while (i < ageGroup.size() - 1) {
                    Person person1 = ageGroup.get(i);
                    Person person2 = ageGroup.get(i + 1);

                    // Ensure no couple without a kitchen and no couple with the same kitchen
                    if (person1.hasKitchen() && person2.hasKitchen() && !person1.getKitchen().equals(person2.getKitchen())) {
                        person1.setPartner(person2);
                        person2.setPartner(person1);
                        newCouples.add(person1);
                        newCouples.add(person2);
                        i += 2; // Move to the next pair
                    } else {
                        i++; // Try the next person
                    }
                }
            }
        }

        // Update the lists of participants
        allSingleParticipants.removeAll(newCouples);
        allParticipants.clear();
        allParticipants.addAll(allSingleParticipants);
        allParticipants.addAll(newCouples);

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
