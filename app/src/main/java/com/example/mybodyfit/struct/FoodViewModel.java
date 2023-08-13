package com.example.mybodyfit.struct;

public class FoodViewModel {

    private String name;
    private int grams;
    private int calories;

    public FoodViewModel(String name, int grams, int calories) {
        this.name = name;
        this.grams = grams;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getGrams() {
        return grams;
    }

    public int getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
