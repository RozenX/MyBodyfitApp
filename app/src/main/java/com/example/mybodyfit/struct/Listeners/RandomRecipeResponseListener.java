package com.example.mybodyfit.struct.Listeners;


import com.example.mybodyfit.struct.models.randomRecipes.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {

    void didFetch(RandomRecipeApiResponse response, String msg);

    void didError(String msg);
}
