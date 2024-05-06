package model;


import java.util.List;

public class CoupleManager {
    // needs overview over all People, filtered if locked in
    Person[] allParticipants;
    // everyone who is left
    Person[] succeedingParticipants;

    public void giveToGroupManager(GroupManager groupManager) {


    }

    void calcCouples(List<Person> person) {
        //TODO Algorithm to sort people into couples
    }
    public void addPerson() {
        //TODO Algorithm to add a Person, then a calcCouples again
    }

    public void addPerson(Person person){
        // TODO Algorithm to add a Person, then a calcCouples again
    }

    public void removePerson(Person person) {
        //TODO removes Person and runs calcCouples again
    }
    public Person getPerson(String string) {
        //TODO returns a Person by their ID
        return null;
    }
    public Person[] getCouple(String string) {
        //TODO returns a Couple by their ID ( or one of the Peoples ID)<-maybe dont know
        return null;
    }

    public static class Couple {

    }
}
