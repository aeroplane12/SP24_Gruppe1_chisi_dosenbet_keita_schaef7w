package model;

public class Kitchen extends Location {
    private Double story;
    private boolean emergency;

    Kitchen(String[] strings){
        super(Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
        emergency = !strings[0].equals("yes");
        story = strings[1].isEmpty()? null : Double.parseDouble(strings[1]);
    }

    /**
     *
     * @param longitude other location
     * @param latitude
     * @param emergency
     * @param story
     */
    Kitchen(Double longitude, Double latitude, boolean emergency, Double story) {
        super(longitude,latitude);
        this.emergency = emergency;
        this.story = story;
    }

    @Override
    public int distance(Location l){
        //TODO calculation of distance between two Kitchens,
        // necessary for determining identical Kitchens
        return 0;
    }

    @Override
    public String toString() {
        return "[Kitchen_Status: " +
                (emergency ? "only for emergencies" : "always available") +
                ", Kitchen_Longitude: " + longitude +
                ", Kitchen_Latitude: " + latitude +
                (story != null ? ", Kitchen_Story: " + story : "") +
                "]";
    }
}
