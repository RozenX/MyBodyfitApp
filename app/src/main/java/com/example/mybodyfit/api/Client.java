package com.example.mybodyfit.api;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    @SuppressLint("StaticFieldLeak")
    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024)) // 10MB cache
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .callbackExecutor(executorService)
                    .build();

        }
        return retrofit;
    }
}
