package com.example.mybodyfit.constants;

import com.example.mybodyfit.struct.FoodModel;

import java.util.ArrayList;
import java.util.Arrays;

public class AppConstants {

    public static class Weight {

        static final int CALORIES_OF_FAT = 7700;

        public static double deficitToFatLoss(double maintenance, double caloricIntake) {
            double weeklyDeficit = 7 * (maintenance - caloricIntake);
            return (double) weeklyDeficit / CALORIES_OF_FAT;
        }

        public static double surplusToFatGain(double maintenance, double caloricIntake) {
            double weeklySurplus = 7 * (caloricIntake - maintenance);
            return (double) weeklySurplus / CALORIES_OF_FAT;
        }
    }

    public static class NutrientBreakDown {

        public static final int CALORIES_IN_GRAM_PROTEIN = 4;
        public static final int CALORIES_IN_GRAM_CARB = 4;
        public static final int CALORIES_IN_GRAM_FAT = 9;
    }

    public static class FoodViewSpinnerListConst {

        private static final String[] breakfast = {"Breakfast", "Lunch", "Dinner", "Snacks"};
        private static final String[] lunch = {"Lunch", "Breakfast", "Dinner", "Snacks"};
        private static final String[] dinner = {"Dinner", "Breakfast", "Lunch", "Snacks"};
        private static final String[] snacks = {"Snacks", "Breakfast", "Lunch", "Dinner"};

        public static ArrayList<CharSequence> getListAdapter(int meal) {
            ArrayList<CharSequence> meals = new ArrayList<>();
            if (meal == FoodModel.BREAKFAST) {
                meals.addAll(Arrays.asList(breakfast));
            } else if (meal == FoodModel.LUNCH) {
                meals.addAll(Arrays.asList(lunch));
            } else if (meal == FoodModel.DINNER) {
                meals.addAll(Arrays.asList(dinner));
            } else if (meal == FoodModel.SNACK) {
                meals.addAll(Arrays.asList(snacks));
            }
            return meals;
        }
    }
}
