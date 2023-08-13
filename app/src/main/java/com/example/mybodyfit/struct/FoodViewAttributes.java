package com.example.mybodyfit.struct;

public class FoodViewAttributes {

    private final String name;
    private final String calories;
    private final String amount;

    public FoodViewAttributes(String name, String calories, String amount) {
        this.name = name;
        this.calories = calories;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getAmount() {
        return amount;
    }
}
