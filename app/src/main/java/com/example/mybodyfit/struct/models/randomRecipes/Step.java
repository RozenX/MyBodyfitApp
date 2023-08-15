package com.example.mybodyfit.struct.models.randomRecipes;

import java.io.Serializable;
import java.util.ArrayList;

public class Step implements Serializable {

    public int number;
    public String step;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Equipment> equipment;
}
