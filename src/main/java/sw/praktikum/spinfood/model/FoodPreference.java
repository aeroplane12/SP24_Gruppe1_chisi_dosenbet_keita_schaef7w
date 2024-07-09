package sw.praktikum.spinfood.model;

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

        @Override
        public String toString() {
            return switch (this.value) {
                case 3 -> "VEGAN";
                case 2 -> "VEGGIE";
                case 1 -> "MEAT";
                default -> "ANY";
            };
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
