package com.example.mybodyfit.dataBase.repositries;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.dao.FoodDao;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.struct.CurrentDate;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodsRepository {

    private final FoodDao foodDao;
    private final MyBodyDatabase database;
    private final LiveData<List<Foods>> allFoods;

    public FoodsRepository(Application app) {
        database = MyBodyDatabase.getInstance(app);
        foodDao = database.foodDao();
        allFoods = foodDao.pullAll();
    }

    public void insert(Foods food) {
        new InsertFoodTask(foodDao).execute(food).isCancelled();
    }

    public void deleteAll() {
        new DeleteAllFoodsTask(foodDao, database);
    }


    public void deleteByMeal(Foods food) {
        new DeleteFoodByMealTask(foodDao).execute(food);
    }

    public List<Foods> pullAllData() {
        try {
            return new PullAllFoodsDataTask(foodDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    public LiveData<List<Foods>> pullAll() {
        return allFoods;
    }

    public LiveData<List<Foods>> pullByMealAndDate(int mealTime, String date) {
        return foodDao.pullByMealAndDate(mealTime, date);
    }

    public List<Foods> pullByMealAndDateData(int mealTime, String date) {
        try {
            return new PullFoodByMealAndDateDataTask(foodDao, mealTime, date).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static class InsertFoodTask extends AsyncTask<Foods, Void, Void> {
        private final FoodDao foodDao;

        private InsertFoodTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Foods... foods) {
            foodDao.insert(foods[0]);
            return null;
        }
    }

    private static class DeleteAllFoodsTask extends AsyncTask<Foods, Void, Void> {
        private final FoodDao foodDao;
        private final MyBodyDatabase db;

        private DeleteAllFoodsTask(FoodDao foodDao, MyBodyDatabase db) {
            this.foodDao = foodDao;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Foods... foods) {
            foodDao.deleteAll();
            db.clearAllTables();
            return null;
        }
    }

    private static class DeleteFoodByMealTask extends AsyncTask<Foods, Void, Void> {
        private final FoodDao foodDao;

        private DeleteFoodByMealTask(FoodDao foodDao) {
            this.foodDao = foodDao;

        }

        @Override
        protected Void doInBackground(Foods... foods) {
            foodDao.deleteByMeal(foods[0]);
            return null;
        }
    }

    private static class PullFoodsTask extends AsyncTask<Foods, Void, Void> {
        private final FoodDao foodDao;

        private PullFoodsTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Foods... foods) {
            foodDao.pullAll();
            return null;
        }
    }

    private static class PullFoodByMealAndDateTask extends AsyncTask<Foods, Void, Void> {
        private final FoodDao foodDao;

        private PullFoodByMealAndDateTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Foods... foods) {
            foodDao.pullByMealAndDate(foods[0].getMealTime(), CurrentDate.getDateWithoutTimeUsingCalendar());
            return null;
        }
    }

    private static class PullFoodByMealAndDateDataTask extends AsyncTask<Void, Void, List<Foods>> {

        private final FoodDao foodDao;
        private final int mealTime;
        private final String date;

        private PullFoodByMealAndDateDataTask(FoodDao foodDao, int mealTime, String date) {
            this.foodDao = foodDao;
            this.mealTime = mealTime;
            this.date = date;
        }

        @Override
        protected List<Foods> doInBackground(Void... voids) {
            return foodDao.pullByMealAndDateData(mealTime, date);
        }
    }

    private static class PullAllFoodsDataTask extends AsyncTask<Void, Void, List<Foods>> {

        private final FoodDao foodDao;

        private PullAllFoodsDataTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected List<Foods> doInBackground(Void... voids) {
            return foodDao.pullAllData();
        }
    }
}
