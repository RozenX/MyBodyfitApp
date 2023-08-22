package com.example.mybodyfit.dataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.mybodyfit.dataBase.dao.FoodDao;
import com.example.mybodyfit.dataBase.dao.SettingsDao;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.dataBase.entities.SettingsPreference;

@Database(entities = {Foods.class, SettingsPreference.class}, version = 6)
public abstract class MyBodyDatabase extends RoomDatabase {

    private static MyBodyDatabase instance = null;

    public abstract FoodDao foodDao();

    public abstract SettingsDao settingsDao();

    public static synchronized MyBodyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyBodyDatabase.class, "myBody_database")
                    .addCallback(roomCallBack)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDBTask extends AsyncTask<Void, Void ,Void> {

        private FoodDao foodDao;

        private PopulateDBTask(MyBodyDatabase db) {
            foodDao = db.foodDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //todo if i want to init the data base with a food
            return null;
        }
    }
}
