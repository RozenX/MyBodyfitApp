package com.example.mybodyfit.struct.models.randomRecipes;

import java.io.Serializable;
import java.util.ArrayList;

public class AnalyzedInstruction implements Serializable {

    public String name;
    public ArrayList<Step> steps;
}
