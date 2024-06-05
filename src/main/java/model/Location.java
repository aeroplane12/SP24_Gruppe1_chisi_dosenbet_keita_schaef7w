package model;

public class Location {
    private final static int R = 6371000 ;
    Double longitude;
    Double latitude;
    public Location(Double longitude, Double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    /**
     * distance
     * @param l other location
     * @return the approximate distance between the two locations
     */
    public Double distance(Location l){
        Double lat1Rad = Math.toRadians(latitude);
        Double lat2Rad = Math.toRadians(l.latitude);
        Double lon1Rad = Math.toRadians(longitude);
        Double lon2Rad = Math.toRadians(l.longitude);
        Double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        Double y = (lat2Rad - lat1Rad);
        return Math.sqrt(x * x + y * y) * R;
    }

}
