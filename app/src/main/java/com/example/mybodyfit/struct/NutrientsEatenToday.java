package com.example.mybodyfit.struct;

public class NutrientsEatenToday {

    public static int calories = 0;
    public static int protein = 0;
    public static int carbs = 0;
    public static int fats = 0;
    private static String currentDate = CurrentDate.getDateWithoutTimeUsingCalendar();

    public static void rest() {
        if (!currentDate.equals(CurrentDate.getDateWithoutTimeUsingCalendar())) {
            currentDate = CurrentDate.getDateWithoutTimeUsingCalendar();
            calories = 0;
            protein = 0;
            carbs = 0;
            fats = 0;
        }
    }
}
