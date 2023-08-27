package com.example.mybodyfit.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mybodyfit.dataBase.entities.Foods;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Foods food);

    @Query("DELETE FROM foods_table")
    void deleteAll();

    @Delete
    void deleteByMeal(Foods food);

    @Query("SELECT * FROM foods_table;")
    List<Foods> pullAllData();

    @Query("SELECT * FROM foods_table;")
    LiveData<List<Foods>> pullAll();

    @Query("SELECT * FROM foods_table WHERE mealTime = :mealTime AND date = :date;")
    LiveData<List<Foods>> pullByMealAndDate(int mealTime, String date);

    @Query("SELECT * FROM foods_table WHERE mealTime = :mealTime AND date = :date;")
    List<Foods> pullByMealAndDateData(int mealTime, String date);
}
