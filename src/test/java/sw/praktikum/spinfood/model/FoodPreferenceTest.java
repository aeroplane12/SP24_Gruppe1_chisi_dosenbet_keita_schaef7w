package sw.praktikum.spinfood.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodPreferenceTest {

    @Test
    public void testGetFoodPref_None() {
        String foodPrefString = "none";
        FoodPreference.FoodPref expected = FoodPreference.FoodPref.NONE;
        FoodPreference.FoodPref actual = FoodPreference.getFoodPref(foodPrefString);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFoodPref_Meat() {
        String foodPrefString = "meat";
        FoodPreference.FoodPref expected = FoodPreference.FoodPref.MEAT;
        FoodPreference.FoodPref actual = FoodPreference.getFoodPref(foodPrefString);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFoodPref_Veggie() {
        String foodPrefString = "veggie";
        FoodPreference.FoodPref expected = FoodPreference.FoodPref.VEGGIE;
        FoodPreference.FoodPref actual = FoodPreference.getFoodPref(foodPrefString);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFoodPref_Vegan() {
        String foodPrefString = "vegan";
        FoodPreference.FoodPref expected = FoodPreference.FoodPref.VEGAN;
        FoodPreference.FoodPref actual = FoodPreference.getFoodPref(foodPrefString);
        assertEquals(expected, actual);
    }
}