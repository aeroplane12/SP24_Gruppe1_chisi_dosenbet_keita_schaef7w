package model;

public class Group {
    private final int ID;

    Couple hosts;
    Person[] guests;
    float avgGender;
    float avgAgeRange;
    Course course;

    FoodPreference.FoodPref foodPreference;

    public Group(Couple hosts, int id, Course course){
        this.ID = id;
        this.hosts = hosts;
        this.course = course;
    }

    public int getID() {
        return ID;
    }
    public Couple getHosts() {
        return hosts;
    }
    public Course getCourse() {
        return course;
    }
}
