package com.example.mybodyfit.struct.Listeners;

import com.example.mybodyfit.struct.models.nutrients.FoodNutrientsApiResponseSP;

public interface FoodNutrientsResponseListener {

    void didFetch(FoodNutrientsApiResponseSP response, String msg);

    void didError(String msg);
}
