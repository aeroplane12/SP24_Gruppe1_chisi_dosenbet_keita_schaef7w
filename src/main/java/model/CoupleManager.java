package model;

import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class CoupleManager {


    private static CoupleManager instance;
    public static CoupleManager getInstance(){
        if(instance == null)
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

    void calcCouples(){

        public void calcCouples() {
            // Create a graph
            SimpleWeightedGraph<Person, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

            // Add vertices
            for (Person person : allSingleParticipants) {
                graph.addVertex(person);
            }

            // Add edges with weights (costs)
            for (Person person1 : allSingleParticipants) {
                for (Person person2 : allSingleParticipants) {
                    if (!person1.equals(person2)) {
                        DefaultWeightedEdge edge = graph.addEdge(person1, person2);
                        double cost = calculateCost(person1, person2); // Implement this method based on your criteria
                        graph.setEdgeWeight(edge, cost);
                    }
                }
            }

            // Apply the Hungarian algorithm
            KuhnMunkresMinimalWeightBipartitePerfectMatching<Person, DefaultWeightedEdge> matching =
                    new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(graph, allSingleParticipants, allSingleParticipants);

            // Get the matching and create couples
            for (DefaultWeightedEdge edge : matching.getMatching()) {
                Person person1 = graph.getEdgeSource(edge);
                Person person2 = graph.getEdgeTarget(edge);
                Couple couple = new Couple(person1, person2);
                couples.add(couple);
            }
        }


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
