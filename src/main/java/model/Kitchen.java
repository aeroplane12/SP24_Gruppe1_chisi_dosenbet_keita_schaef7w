package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Kitchen extends Location {
    private double story;
    private boolean emergency;
    private Map<Course,Boolean> inUse = Map.of(
            Course.STARTER,false,
            Course.DINNER,false,
            Course.DESSERT,false);
    private List<Person> owner = new ArrayList<>();

    Kitchen(String[] strings){
        super(Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
        emergency = strings[0].equals("maybe");
        story = strings[1].isEmpty()? 0d : Double.parseDouble(strings[1]);
    }


    Kitchen(double longitude,double latitude,boolean emergency,double story){
        super(longitude,latitude);
        this.emergency = emergency;
        this.story = story;
    }

    @Override
    public double distance(Location l){
        double x = super.distance(l);
        if ((x==0 && (l instanceof Kitchen))){
            return Math.abs(((Kitchen) l).story-story);
        }
        return x;
    }

    @Override
    public String toString() {
        return "[Kitchen_Status: " +
                (emergency ? "only for emergencies" : "always available") +
                ", Kitchen_Longitude: " + longitude +
                ", Kitchen_Latitude: " + latitude +
                ", Kitchen_Story: " + story +
                "]";
    }

    /**
     * setUse
     * sets an inUse tag at the specified time on the kitchen
     * @param c the course for which the kitchen is to be booked
     * @return whether the setting was successful
     */
    public boolean setUse(Course c){
        if (!inUse.get(c)){
            inUse.remove(c,true);
            return true;
        }
        return false;
    }

    /**
     * determines if the kitchen is available at that time
     * @param c the "timeslot"
     * @return availability
     */
    public boolean checkUse(Course c){
        return inUse.get(c);
    }
    public boolean isEmergency() {
        return emergency;
    }
    /**
     * add
     * adding an owner to a Kitchen
     * @param person an owner of this kitchen
     */
    public void addOwner(Person person){
        owner.add(person);
    }
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Kitchen otherKitchen)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        return this.latitude == otherKitchen.latitude &&
                this.longitude == otherKitchen.longitude &&
                this.story == otherKitchen.story;
        // not really valid for our purposes need them to evaluate based on actual distance
    }
    @Override
    public int hashCode() {
        return Objects.hash(latitude,longitude,story);
    }
}
