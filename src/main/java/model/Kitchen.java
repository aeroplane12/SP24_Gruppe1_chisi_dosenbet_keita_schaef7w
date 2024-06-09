package model;

import java.util.*;
import java.util.stream.Collectors;

public class Kitchen extends Location {
    private double story;
    private boolean emergency;
    private Map<Course,Boolean> inUse = new HashMap<>(Map.of(
            Course.STARTER,false,
            Course.DINNER,false,
            Course.DESSERT,false));
    private Map<Course,Integer> usedBy = new HashMap<>(Map.of(
            Course.STARTER,-1,
            Course.DINNER,-1,
            Course.DESSERT,-1));
    private Set<Person> owner = new HashSet<>(); // hashset contains complexity O(1)

    Kitchen(String[] strings){
        super(Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
        emergency = strings[0].equals("maybe");
        story = strings[1].isEmpty()? 0d : Double.parseDouble(strings[1]);
    }


    public Kitchen(Double longitude, Double latitude, boolean emergency, Double story){
        super(longitude,latitude);
        this.emergency = emergency;
        this.story = story;
    }

    @Override
    public Double distance(Location l){
        Double x = super.distance(l);
        if ((x==0 && (l instanceof Kitchen k))){
            return Math.abs(k.story-story)*3;// assumes a story to be ~3m in height
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
     * @return whether the setting was successful or if its already booked
     */
    public boolean setUse(Course c){
        if (!inUse.get(c)){
            inUse.put(c,true);
            return true;
        }
        return false;
    }

    public boolean isEmergency() {
        return emergency;
    }

    /**
     * Merges this Kitchen with the given parameter Kitchen
     * @param k kitchen to be merged with
     */
    public void mergeKitchen(Kitchen k){
        k.owner = k.owner.stream().peek(x->x.setKitchen(this)).collect(Collectors.toSet());
        owner.addAll(k.owner);
    }

    /**
     * isOwner
     * determines if the given person is an owner of the kitchen
     * @param person given Person
     * @return true if person is an Owner
     */
    public boolean isOwner(Person person){
        return owner.contains(person);
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
        return Objects.equals(this.latitude, otherKitchen.latitude) &&
                Objects.equals(this.longitude, otherKitchen.longitude) &&
                this.story == otherKitchen.story;
        // not really valid for our purposes need them to evaluate based on actual distance
    }
    @Override
    public int hashCode() {
        return Objects.hash(latitude,longitude,story);
    }


    /**
     * checkUser
     * @param course the time
     * @param id the CoupleID
     * @return whether the person is using the kitchen at that time
     */
    public boolean checkUser(Course course,int id){
        return usedBy.get(course)==id;
    }

    /**
     * sets the KitchenUser at the specified time
     * @param course the time
     * @param id the coupleID using the  Kitchen at that time
     * @return true if it was successful
     */
    public boolean setUser(Course course,int id){
        if (usedBy.get(course) == -1) {
            usedBy.put(course,id);
            return true;
        }
        return false;
    }
}
