package com.example.mybodyfit.constants;

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

        public static String[] getListAdapter() {

            return breakfast;
        }
    }
}
