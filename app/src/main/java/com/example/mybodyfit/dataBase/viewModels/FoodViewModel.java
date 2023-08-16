package com.example.mybodyfit.dataBase.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.dataBase.repositries.FoodsRepository;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private final FoodsRepository foodsRepository;
    private final LiveData<List<Foods>> allFoods;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        foodsRepository = new FoodsRepository(application);
        allFoods = foodsRepository.pullAll();

    }

    public void insert(Foods food) {
        foodsRepository.insert(food);
    }


    public void deleteAll() {
        foodsRepository.deleteAll();
    }


    public void deleteByMeal(Foods food) {
        foodsRepository.deleteByMeal(food);
    }


    public LiveData<List<Foods>> pullAll() {
        return allFoods;
    }

    public LiveData<List<Foods>> pullByMealAndDate(int mealTime, String date) {
        return foodsRepository.pullByMealAndDate(mealTime, date);
    }

    public List<Foods> pullByMealAndDateData(int mealTime, String date) {
        return foodsRepository.pullByMealAndDateData(mealTime, date);
    }
}
