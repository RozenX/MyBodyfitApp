package com.example.mybodyfit.dataBase.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.mybodyfit.dataBase.entities.SettingsPreference;

@Dao
public interface SettingsDao {

    @Insert
    void insert(SettingsPreference settingsPreference);

    @Update
    void updatePreference(SettingsPreference settingsPreference);
}
