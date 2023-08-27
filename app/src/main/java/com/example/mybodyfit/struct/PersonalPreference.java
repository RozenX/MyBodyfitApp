package com.example.mybodyfit.struct;

public class PersonalPreference {

    public boolean isToNotifications;
    public String breakfastNotification;
    public String lunchNotification;
    public String dinnerNotification;
    public int currentLogStreak;
    public int bestLogSteak;
    public int caloricGoal;
    public int proteinGoal;
    public int carbGoal;
    public int fatGoal;

    public void setDefaultSettings() {
        isToNotifications = true;
        breakfastNotification = "9:00";
        lunchNotification = "15:00";
        dinnerNotification = "20:00";
        currentLogStreak = 0;
        bestLogSteak = 0;
        caloricGoal = 2000;
        proteinGoal = 150;
        carbGoal = 200;
        fatGoal = 67;
    }
}
