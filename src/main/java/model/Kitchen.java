package model;

public class Kitchen extends Location {
    private Double story;
    private boolean emergency;
    private boolean[] inUse = new boolean[3];
    private Person[] owner;

    Kitchen(String[] strings){
        super(Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
        emergency = strings[0].equals("maybe");
        story = strings[1].isEmpty()? null : Double.parseDouble(strings[1]);
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
        if (x==0 && (l instanceof Kitchen)){
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
                (story != null ? ", Kitchen_Story: " + story : "") +
                "]";
    }

    /**
     * setUse
     * sets an inUse tag at the specified time on the kitchen
     * @param c the course for which the kitchen is to be booked
     * @return whether the setting was successful
     */
    public boolean setUse(Course c){
        if(owner.length<2 && (inUse[0]||inUse[1]||inUse[2]))
            return false; // a single person may not be allowed to host twice, this check might be redundant.
        if (c.equals(Course.STARTER)&&!inUse[0]){
            inUse[0]=true;
            return true;
        } else if (c.equals(Course.DINNER)&&!inUse[1]) {
            inUse[1]=true;
            return true;
        } else if (c.equals(Course.DESSERT)&&!inUse[2]){
            inUse[2]=true;
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
        if(owner.length<2 && (inUse[0]||inUse[1]||inUse[2]))
            return false; // a single person may not be allowed to host twice, this check might be redundant.
        return switch (c){
            case STARTER -> inUse[0];
            case DINNER -> inUse[1];
            case DESSERT -> inUse[2];
        };
    }
}
