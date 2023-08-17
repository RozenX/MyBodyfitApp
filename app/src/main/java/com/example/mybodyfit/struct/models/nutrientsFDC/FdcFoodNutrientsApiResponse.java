package com.example.mybodyfit.struct.models.nutrientsFDC;

import java.util.ArrayList;

public class FdcFoodNutrientsApiResponse {

    public String discontinuedDate;
    public ArrayList<Object> foodComponents;
    public ArrayList<FoodAttribute> foodAttributes;
    public ArrayList<Object> foodPortions;
    public int fdcId;
    public String description;
    public String publicationDate;
    public ArrayList<FoodNutrient> foodNutrients;
    public String dataType;
    public String foodClass;
    public String modifiedDate;
    public String availableDate;
    public String brandOwner;
    public String brandName;
    public String dataSource;
    public String brandedFoodCategory;
    public String gtinUpc;
    public String ingredients;
    public String marketCountry;
    public double servingSize;
    public String servingSizeUnit;
    public String packageWeight;
    public ArrayList<FoodUpdateLog> foodUpdateLog;
    public LabelNutrients labelNutrients;
}
