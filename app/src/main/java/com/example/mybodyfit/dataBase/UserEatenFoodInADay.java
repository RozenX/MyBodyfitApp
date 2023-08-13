package com.example.mybodyfit.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mybodyfit.struct.FoodModel;

import java.util.ArrayList;

public class UserEatenFoodInADay extends SQLiteOpenHelper {

    private static UserEatenFoodInADay instance = null;
    public static final String FOOD_TABLE = "FOODS";
    public static final String FOOD_NAME = "FOOD_NAME";
    public static final String CALORIES = "CALORIES";
    public static final String PROTEIN = "PROTEIN";
    public static final String CARBS = "CARBS";
    public static final String FATS = "FATS";
    public static final String MEAL_TIME = "MEAL_TIME";
    public static final String AMOUNT = "AMOUNT";

    private UserEatenFoodInADay(@Nullable Context context) {
        super(context, "ToDayFoods.db", null, 1);
    }

    public static UserEatenFoodInADay getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new UserEatenFoodInADay(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + FOOD_TABLE + " (" + FOOD_NAME + " TEXT PRIMARY KEY, " + CALORIES + " REAL, " + PROTEIN + " REAL, " + CARBS + " REAL, " + FATS + " REAL, " + AMOUNT + " REAL, " + MEAL_TIME + " TEXT)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addFood(FoodModel food) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FOOD_NAME, food.getName());
        cv.put(CALORIES, food.getCalories());
        cv.put(PROTEIN, food.getProtein());
        cv.put(CARBS, food.getCarbs());
        cv.put(FATS, food.getFats());
        cv.put(MEAL_TIME, food.getTime());
        cv.put(AMOUNT, food.getAmount());

        long insert = db.insert(FOOD_TABLE, null, cv);
        return insert != -1;
    }

    public ArrayList<FoodModel> pullFoods(int meal) {
        ArrayList<FoodModel> foods = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + FOOD_TABLE + " WHERE MEAL_TIME =" + meal + ";";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(FOOD_NAME));
                @SuppressLint("Range") String calories = cursor.getString(cursor.getColumnIndex(CALORIES));
                @SuppressLint("Range") String protein = cursor.getString(cursor.getColumnIndex(PROTEIN));
                @SuppressLint("Range") String carbs = cursor.getString(cursor.getColumnIndex(CARBS));
                @SuppressLint("Range") String fats = cursor.getString(cursor.getColumnIndex(FATS));
                @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex(AMOUNT));
                @SuppressLint("Range") String mealTime = cursor.getString(cursor.getColumnIndex(MEAL_TIME));

                foods.add(new FoodModel(name,
                        Double.parseDouble(calories),
                        Double.parseDouble(protein),
                        Double.parseDouble(carbs),
                        Double.parseDouble(fats),
                        Integer.parseInt(mealTime))
                        .setAmount(Double.parseDouble(amount)));
                cursor.moveToNext();
            }
        }
        return foods;
    }

    public ArrayList<FoodModel> pullFoods() {
        ArrayList<FoodModel> foods = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + FOOD_TABLE + ";";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(FOOD_NAME));
                @SuppressLint("Range") String calories = cursor.getString(cursor.getColumnIndex(CALORIES));
                @SuppressLint("Range") String protein = cursor.getString(cursor.getColumnIndex(PROTEIN));
                @SuppressLint("Range") String carbs = cursor.getString(cursor.getColumnIndex(CARBS));
                @SuppressLint("Range") String fats = cursor.getString(cursor.getColumnIndex(FATS));
                @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex(AMOUNT));
                @SuppressLint("Range") String mealTime = cursor.getString(cursor.getColumnIndex(MEAL_TIME));

                foods.add(new FoodModel(name,
                        Double.parseDouble(calories),
                        Double.parseDouble(protein),
                        Double.parseDouble(carbs),
                        Double.parseDouble(fats),
                        Integer.parseInt(mealTime))
                        .setAmount(Double.parseDouble(amount)));
                cursor.moveToNext();
            }
        }
        return foods;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + FOOD_TABLE;
        db.execSQL(query);
    }

    public void deleteFood(String foodName) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + FOOD_TABLE + " WHERE " + FOOD_NAME +"=" + foodName +";";
        db.execSQL(query);
    }
}
