package model;

public class Location {
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
    public int distance(Location l){
        //TODO calculation of distance between two locations
        return 0;
    }

}
