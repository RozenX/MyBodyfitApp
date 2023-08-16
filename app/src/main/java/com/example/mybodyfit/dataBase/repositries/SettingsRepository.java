package com.example.mybodyfit.dataBase.repositries;

import android.app.Application;
import android.os.AsyncTask;

import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.dao.SettingsDao;
import com.example.mybodyfit.dataBase.entities.SettingsPreference;

public class SettingsRepository {

    private SettingsDao settingsDao;

    public SettingsRepository(Application app) {
        settingsDao = MyBodyDatabase.getInstance(app).settingsDao();
    }

    void insert(SettingsPreference settingsPreference) {
        new InsertSettingsPreferenceTask(settingsDao).execute(settingsPreference);
    }

    void updatePreference(SettingsPreference settingsPreference) {
        new UpdatePreferenceTask(settingsDao).execute(settingsPreference);
    }

    private static class InsertSettingsPreferenceTask extends AsyncTask<SettingsPreference, Void, Void> {

        private SettingsDao settingsDao;

        public InsertSettingsPreferenceTask(SettingsDao settingsDao) {
            this.settingsDao = settingsDao;
        }

        @Override
        protected Void doInBackground(SettingsPreference... settingsPreferences) {
            settingsDao.insert(settingsPreferences[0]);
            return null;
        }
    }

    private static class UpdatePreferenceTask extends AsyncTask<SettingsPreference, Void, Void> {

        private SettingsDao settingsDao;

        public UpdatePreferenceTask(SettingsDao settingsDao) {
            this.settingsDao = settingsDao;
        }

        @Override
        protected Void doInBackground(SettingsPreference... settingsPreferences) {
            settingsDao.updatePreference(settingsPreferences[0]);
            return null;
        }
    }
}
