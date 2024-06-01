package model;

public class Couple {
    private final Person person1;
    private final Person person2;

    public Couple(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }
}