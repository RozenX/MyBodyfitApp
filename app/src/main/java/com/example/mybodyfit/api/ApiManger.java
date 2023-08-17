package com.example.mybodyfit.api;

import android.content.Context;

import com.example.mybodyfit.struct.Listeners.FdcFoodSearchResponseListener;
import com.example.mybodyfit.struct.Listeners.FdcNutrientsResponseListener;
import com.example.mybodyfit.struct.models.nutrientsFDC.FdcFoodNutrientsApiResponse;
import com.example.mybodyfit.struct.models.searchFoodFDC.FdcSearchApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiManger {

    private static final String API_KEY = "EacVx94TR0zySAQInh2yIVbhxCqoQkQPAOM1vckj";
    private Context context;

    public ApiManger(Context context) {
        this.context = context;
    }

    public void searchFoods(FdcFoodSearchResponseListener listener, String query) {
        FdcSearch apiService = Client.getClient(context).create(FdcSearch.class);
        Call<FdcSearchApiResponse> call = apiService.searchFoods(query, "relevance", API_KEY);

        call.enqueue(new Callback<FdcSearchApiResponse>() {
            @Override
            public void onResponse(Call<FdcSearchApiResponse> call, Response<FdcSearchApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<FdcSearchApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getFoodsNutrients(FdcNutrientsResponseListener listener, int fdcId) {
        FdcNutrients apiService = Client.getClient(context).create(FdcNutrients.class);
        Call<FdcFoodNutrientsApiResponse> call = apiService.getFoodNutrients(fdcId, 1, API_KEY);

        call.enqueue(new Callback<FdcFoodNutrientsApiResponse>() {
            @Override
            public void onResponse(Call<FdcFoodNutrientsApiResponse> call, Response<FdcFoodNutrientsApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<FdcFoodNutrientsApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface FdcSearch {

        @GET("v1/foods/search")
        Call<FdcSearchApiResponse> searchFoods(@Query("query") String query, @Query("sort") String sort, @Query("api_key") String apiKey);
    }

    private interface FdcNutrients {

        @GET("v1/food/{fdcId}")
        Call<FdcFoodNutrientsApiResponse> getFoodNutrients(@Path("fdcId") int fdcId, @Query("amount") double amount, @Query("api_key") String apiKey);
    }
}
