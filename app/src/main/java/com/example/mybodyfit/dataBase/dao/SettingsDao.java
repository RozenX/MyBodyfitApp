package com.example.mybodyfit.dataBase.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface SettingsDao {

    @Insert
    void insert(SettingsPreference settingsPreference);

    @Update
    void updatePreference(SettingsPreference settingsPreference);
}
