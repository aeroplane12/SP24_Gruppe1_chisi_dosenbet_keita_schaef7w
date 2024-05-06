package model;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CoupleManager {
    // needs overview over all People
    Person[] allParticipants;
    // everyone who is not locked in left
    Person[] allSingleParticipants;
    // everyone who is left
    Person[] succeedingParticipants;

    void calcCouples(){
        //TODO Algorithm to sort people into couples
        // needs to be in accordance with the specifications:
        if(Arrays.stream(allParticipants).allMatch(Person::isLockedIn))
            return;


    }
    public void addPerson(Person person){
        if (person.hasPartner()) {
            allParticipants = Arrays.copyOf(allParticipants,allParticipants.length+2);
            allParticipants[allParticipants.length-2] = person;
            allParticipants[allParticipants.length-1] = person.getPartner();
        } else {
            allParticipants = Arrays.copyOf(allParticipants,allParticipants.length+1);
            allParticipants[allParticipants.length-1] = person;
        }
        calcCouples();
    }

    public void removePerson(String personID){
        allParticipants = (Person[]) Arrays.stream(allParticipants)
                .filter(x->!x.getID().equals(personID))
                .peek(x->{
                    if(x.getPartner().getID().equals(personID)) {
                    x.setPartner(null);
                    x.setLockedIn(false);
                    }})
                .toArray();
        calcCouples();
    }
    public void removeCouple(String CoupleID){
        allParticipants = (Person[]) Arrays.stream(allParticipants)
                .filter(x->!x.getCoupleIDs().equals(CoupleID))
                .toArray();
    }
    public Person getPerson(String string){
        return Arrays.stream(allParticipants)
                .filter(x->x.getID().equals(string))
                .findFirst()
                .orElse(null);
    }

    /**
     * returns a Couple by their CoupleID
     * @param string the CoupleID
     * @return the Couple
     */
    public Person[] getCouple(String string){
        return Arrays.stream(allParticipants)
                .map(Person::getCouple)
                .filter(Objects::nonNull)
                .filter(x->x[0].getCoupleIDs().equals(string))
                .findFirst()
                .orElse(null);
    }
}
