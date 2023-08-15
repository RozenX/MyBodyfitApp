package com.example.mybodyfit.struct.models.randomRecipes;

import java.io.Serializable;
import java.util.ArrayList;

public class ExtendedIngredient implements Serializable {

    public int id;
    public String aisle;
    public String image;
    public String consistency;
    public String name;
    public String nameClean;
    public String original;
    public String originalName;
    public double amount;
    public String unit;
    public ArrayList<String> meta;
    public Measures measures;
}
