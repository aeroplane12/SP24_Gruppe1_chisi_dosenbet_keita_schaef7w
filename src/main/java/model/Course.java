package model;

public enum Course {
    STARTER(1),
    DINNER(2),
    DESSERT(3);

    public final int value;
    Course(int value) {
        this.value = value;
    }
}
