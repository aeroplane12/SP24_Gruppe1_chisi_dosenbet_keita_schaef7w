package model;

import java.util.Map;

public class Kitchen extends Location {
    private double story;
    private boolean emergency;
    private Map<Course,Boolean> inUse = Map.of(
            Course.STARTER,false,
            Course.DINNER,false,
            Course.DESSERT,false);
    private Person[] owner;

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
        //TODO calculation of distance between two Kitchens,
        // necessary for determining identical Kitchens
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
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Kitchen otherKitchen = (Kitchen) other;
        if (this == other) {
            return true;
        }

        if (this.latitude == otherKitchen.latitude) {
            if (this.longitude == otherKitchen.longitude) {
                return this.story == otherKitchen.story;
            }
        }
        return false;
    }
}
