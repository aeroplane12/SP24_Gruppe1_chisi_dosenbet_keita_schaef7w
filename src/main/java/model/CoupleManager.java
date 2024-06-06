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

    void calcCouples(){
        //TODO Algorithm to sort people into couples
        // needs to be in accordance with the specifications:
        // - Strictly according to their FoodPreference,
        // meaning there should be no couples who dont eat the same food
        // - Sorted according to their Age Group
        // - no couple without a kitchen, no couple with the same kitchen


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

    public List<Person> getSinglesList() {
        return allSingleParticipants;
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
