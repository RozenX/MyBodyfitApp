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
import com.example.mybodyfit.dataBase.firebase.FireBaseConnection;

@Database(entities = {Foods.class}, version = 7)
public abstract class MyBodyDatabase extends RoomDatabase {

    private static MyBodyDatabase instance = null;
    private static FireBaseConnection fireBaseConnection;

    public abstract FoodDao foodDao();

    public abstract SettingsDao settingsDao();

    public static synchronized MyBodyDatabase getInstance(Context context) {
        if (instance == null) {
            FireBaseConnection.init(context);
            fireBaseConnection = FireBaseConnection.getInstance();
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyBodyDatabase.class, "myBody_database")
                    .addCallback(roomCallBack)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public void deleteAllTables() {
        new DeleteAll(this).execute();
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBTask(instance).execute();
        }
    };

    private static class PopulateDBTask extends AsyncTask<Void, Void, Void> {

        private FoodDao foodDao;

        private PopulateDBTask(MyBodyDatabase db) {
            foodDao = db.foodDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FireBaseConnection.getInstance().addFoodsByUser(foodDao);
            return null;
        }
    }

    private static class DeleteAll extends AsyncTask<Void, Void, Void> {

        private MyBodyDatabase db;

        private DeleteAll(MyBodyDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.clearAllTables();
            return null;
        }
    }
}
