package model;

import java.util.List;

public class Group {
    private final int ID;

    private final Couple HOST;
    private final Couple GUEST1;
    private final Couple GUEST2;
    float avgGender;
    float avgAgeRange;
    FoodPreference.FoodPref foodPreference;
    Course course;
    public Group(Couple host,
                 Couple guest1,
                 Couple guest2,
                 Course course,
                 int id){
        HOST = host;
        GUEST1 = guest1;
        GUEST2 = guest2;
        ID = id;
        this.course = course;
    }

    public int getID() {
        return ID;
    }
    public Couple getHosts() {
        return HOST;
    }
    public Course getCourse() {
        return course;
    }
    public List<Couple> getAll(){
        return List.of(HOST,GUEST1,GUEST2);
    }
}
