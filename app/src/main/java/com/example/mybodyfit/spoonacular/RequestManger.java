package com.example.mybodyfit.spoonacular;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.mybody.R;
import com.example.mybodyfit.struct.Listeners.FoodNameResponseListener;
import com.example.mybodyfit.struct.Listeners.FoodNutrientsResponseListener;
import com.example.mybodyfit.struct.Listeners.RandomRecipeResponseListener;
import com.example.mybodyfit.struct.models.nutrients.FoodNutrientsApiResponseSP;
import com.example.mybodyfit.struct.models.randomRecipes.RandomRecipeApiResponse;
import com.example.mybodyfit.struct.models.searchFood.SearchFoodApiResponse;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManger {

    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManger(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener) {
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "25");
        call.enqueue(new Callback<RandomRecipeApiResponse>() {

            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getFoods(FoodNameResponseListener listener, String query) {
        CallFoods callF = retrofit.create(CallFoods.class);
        Call<SearchFoodApiResponse> call = callF.callFoods(context.getString(R.string.api_key), query, "99");
        call.enqueue(new Callback<SearchFoodApiResponse>() {
            @Override
            public void onResponse(Call<SearchFoodApiResponse> call, Response<SearchFoodApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SearchFoodApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getFoodNutrients(FoodNutrientsResponseListener listener, String id) {
        CallNutrients callF = retrofit.create(CallNutrients.class);
        Call<FoodNutrientsApiResponseSP> call = callF.callFoodNutrients(id, context.getString(R.string.api_key), "1", "g");
        call.enqueue(new Callback<FoodNutrientsApiResponseSP>() {
            @Override
            public void onResponse(@NonNull Call<FoodNutrientsApiResponseSP> call, @NonNull Response<FoodNutrientsApiResponseSP> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<FoodNutrientsApiResponseSP> call, @NonNull Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallRandomRecipes {

        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number
        );
    }

    private interface CallFoods {

        @GET("food/ingredients/search")
        Call<SearchFoodApiResponse> callFoods(
                @Query("apiKey") String apiKey,
                @Query("query") String query,
                @Query("number") String number
        );
    }

    private interface CallNutrients {

        @GET("food/ingredients/{id}/information")
        Call<FoodNutrientsApiResponseSP> callFoodNutrients(
                @Path(value = "id") String id,
                @Query("apiKey") String apiKey,
                @Query("amount") String amount,
                @Query("unit") String unit
        );
    }
}
