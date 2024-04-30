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
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[Kitchen_Status: ").append(maybe?"only for emergencies":"always available").append(", Kitchen_Longitude: ").append(longitude)
                .append(", Kitchen_Latitude: ").append(latitude);
        if (story != null) {
            s.append(", Kitchen_Story: ").append(story);
        }
        s.append("]");
        return s.toString();
    }
}
