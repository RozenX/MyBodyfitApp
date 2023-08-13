package com.example.mybodyfit.struct;

public class FoodModel {

    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private double amount;
    private int time;

    public FoodModel(String name, double calories, double protein, double carbs, double fats, int time) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.time = time;
    }

    public FoodModel setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public double getAmount() {
        return amount;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                ", time=" + time +
                '}';
    }

    public static int BREAKFAST = 1;
    public static int LUNCH = 2;
    public static int DINNER = 3;
    public static int SNACK = 4;
}
