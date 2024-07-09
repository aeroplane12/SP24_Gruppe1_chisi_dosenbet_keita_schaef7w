package sw.praktikum.spinfood.model.tools;

import javafx.util.StringConverter;
import sw.praktikum.spinfood.model.FoodPreference;

public class FoodPreferenceStringConverter extends StringConverter<FoodPreference.FoodPref> {
    public FoodPreferenceStringConverter() {
    }
    @Override
    public String toString(FoodPreference.FoodPref foodPref) {
        return foodPref == null ? "" : foodPref.toString();
    }

    @Override
    public FoodPreference.FoodPref fromString(String s) {
        return switch (s) {
            case "ANY" -> FoodPreference.FoodPref.NONE;
            case "MEAT" -> FoodPreference.FoodPref.MEAT;
            case "VEGGIE" -> FoodPreference.FoodPref.VEGGIE;
            case "VEGAN" -> FoodPreference.FoodPref.VEGAN;
            default -> null;
        };
    }
}
