package com.example.mybodyfit.dataBase.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings_preference_table")
public class SettingsPreference {

    @PrimaryKey(autoGenerate = true)
    private boolean toNotification;
    private String breakfastNote;
    private String lunchNote;
    private String dinnerNote;
    private String waterNote;
    private int calories;
    private int protein;
    private int carbs;
    private int fats;

    public SettingsPreference(boolean toNotification, String breakfastNote, String lunchNote,
                              String dinnerNote, String waterNote, int calories, int protein,
                              int carbs, int fats) {
        this.toNotification = toNotification;
        this.breakfastNote = breakfastNote;
        this.lunchNote = lunchNote;
        this.dinnerNote = dinnerNote;
        this.waterNote = waterNote;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }


    public boolean isToNotification() {
        return toNotification;
    }

    public void setToNotification(boolean toNotification) {
        this.toNotification = toNotification;
    }

    public String getBreakfastNote() {
        return breakfastNote;
    }

    public void setBreakfastNote(String breakfastNote) {
        this.breakfastNote = breakfastNote;
    }

    public String getLunchNote() {
        return lunchNote;
    }

    public void setLunchNote(String lunchNote) {
        this.lunchNote = lunchNote;
    }

    public String getDinnerNote() {
        return dinnerNote;
    }

    public void setDinnerNote(String dinnerNote) {
        this.dinnerNote = dinnerNote;
    }

    public String getWaterNote() {
        return waterNote;
    }

    public void setWaterNote(String waterNote) {
        this.waterNote = waterNote;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

}
