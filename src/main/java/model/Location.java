package model;

public class Location {
    private final static int R = 6371000 ;
    double longitude;
    double latitude;
    public Location(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * distance
     * @param l other location
     * @return the approximate distance between the two locations
     */
    public double distance(Location l){
        double lat1Rad = Math.toRadians(latitude);
        double lat2Rad = Math.toRadians(l.latitude);
        double lon1Rad = Math.toRadians(longitude);
        double lon2Rad = Math.toRadians(l.longitude);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        return Math.sqrt(x * x + y * y) * R;
    }

}
