package model;

public class FoodPreference {
    public enum FoodPref{
        NONE(0),
        MEAT(1),
        VEGGIE(2),
        VEGAN(3);

        public final int value;

        FoodPref(int i){
            value = i;
        }
    }

    /**
     *  getFoodPref
     * @param s the String which defines the foodPref of the participant
     * @return the corresponding foodPreference enum
     */
    public static FoodPref getFoodPref(String s){
        return switch (s) {
            case "vegan" -> FoodPref.VEGAN;
            case "meat" -> FoodPref.MEAT;
            case "veggie" -> FoodPref.VEGGIE;
            default -> FoodPref.NONE;
        };
    }

}