package com.example.mybodyfit.dataBase.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "settings_preference_table")
public class SettingsPreference {

    @PrimaryKey (autoGenerate = true)
    private boolean toNotification;
    private String breakfastNote;
    private String lunchNote;
    private String dinnerNote;
    private String waterNote;

    public SettingsPreference(boolean toNotification, String breakfastNote, String lunchNote, String dinnerNote, String waterNote) {
        this.toNotification = toNotification;
        this.breakfastNote = breakfastNote;
        this.lunchNote = lunchNote;
        this.dinnerNote = dinnerNote;
        this.waterNote = waterNote;
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
}