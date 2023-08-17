package com.example.mybodyfit.struct.Listeners;

import com.example.mybodyfit.struct.models.nutrientsFDC.FdcFoodNutrientsApiResponse;

public interface FdcNutrientsResponseListener {

    void didFetch(FdcFoodNutrientsApiResponse response, String msg);

    void didError(String msg);
}
