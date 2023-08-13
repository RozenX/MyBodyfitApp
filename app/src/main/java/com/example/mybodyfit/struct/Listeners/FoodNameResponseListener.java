package com.example.mybodyfit.struct.Listeners;

import com.example.mybodyfit.struct.models.searchFood.SearchFoodApiResponse;

public interface FoodNameResponseListener {

    void didFetch(SearchFoodApiResponse response, String msg);

    void didError(String msg);
}
