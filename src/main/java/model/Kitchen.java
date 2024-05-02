package model;

public class Kitchen extends Location{
    private Double story;
    private boolean maybe;

    Kitchen(String[] strings){
        super(Double.parseDouble(strings[2]),Double.parseDouble(strings[3]));
        maybe = !strings[0].equals("yes");
        story = strings[1].isEmpty()?null:Double.parseDouble(strings[1]);
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
                (maybe ? "only for emergencies" : "always available") +
                ", Kitchen_Longitude: " + longitude +
                ", Kitchen_Latitude: " + latitude +
                (story != null ? ", Kitchen_Story: " + story : "") +
                "]";
    }
}
