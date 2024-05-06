package model;

public class Location {
    private final static int R = 6371000;
    double longitude;
    double latitude;
    Location(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * distance
     * @param l other location
     * @return the approximate distance between the two locations
     */
    public double distance(Location l){
        return 2 * R * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin((l.latitude-latitude)/2),2)) +
                        Math.cos(l.latitude) * Math.cos(latitude) *
                        Math.pow(Math.sin((l.longitude-longitude)/2),2));
    }

}
