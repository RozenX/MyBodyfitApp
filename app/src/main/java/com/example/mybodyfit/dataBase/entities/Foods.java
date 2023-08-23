package com.example.mybodyfit.dataBase.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods_table")
public class Foods {

    @NonNull
    @PrimaryKey()
    private String foodName;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private int mealTime;
    private double amount;
    private String date;

    public Foods() {}

    public Foods(String foodName, double calories, double protein, double carbs, double fats, int mealTime, double amount, String date) {
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.mealTime = mealTime;
        this.amount = amount;
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public int getMealTime() {
        return mealTime;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setMealTime(int mealTime) {
        this.mealTime = mealTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
