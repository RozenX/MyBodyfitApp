package com.example.mybodyfit.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mybodyfit.struct.FoodModel;

import java.util.ArrayList;
import java.util.List;

public final class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper instance = null;
    public static final String FOOD_TABLE = "FOODS";
    public static final String FOOD_NAME = "FOOD_NAME";
    public static final String CALORIES = "CALORIES";
    public static final String PROTEIN = "PROTEIN";
    public static final String CARBS = "CARBS";
    public static final String FATS = "FATS";

    private DataBaseHelper(@Nullable Context context) {
        super(context, "Foods.db", null, 1);
    }

    public static DataBaseHelper getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new DataBaseHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + FOOD_TABLE + " (" + FOOD_NAME + " TEXT PRIMARY KEY, " + CALORIES + " REAL, " + PROTEIN + " REAL, " + CARBS + " REAL, " + FATS + " REAL)";
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

        long insert = db.insert(FOOD_TABLE, null, cv);
        return insert != -1;
    }

    public List<FoodModel> getFoods() {
        ArrayList<FoodModel> foods = new ArrayList<>();
        String query = "SELECT * FROM " + FOOD_TABLE;
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            int calories = cursor.getInt(1);
            int protein = cursor.getInt(2);
            int carbs = cursor.getInt(3);
            int fats = cursor.getInt(4);

            FoodModel foodModel = new FoodModel(name, calories, protein, carbs, fats, 0);
            foods.add(foodModel);
            do {

            } while (cursor.moveToFirst());
        }
        cursor.close();
        db.close();
        return foods;
    }
}
