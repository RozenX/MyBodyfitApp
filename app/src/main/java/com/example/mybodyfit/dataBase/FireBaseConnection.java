package com.example.mybodyfit.dataBase;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

public final class FireBaseConnection {

    UserEatenFoodInADay foodDb;
    private final FirebaseDatabase db;
    private final DatabaseReference reference;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    FirebaseAuth firebaseAuth;
    private static FireBaseConnection instance = null;

    @Singleton
    public static FireBaseConnection getInstance() {
        return instance;
    }

    private FireBaseConnection(DatabaseReference reference, Context context) {
        this.reference = reference;
        this.context = context;
        db = FirebaseDatabase.getInstance();
        foodDb = UserEatenFoodInADay.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static void init(DatabaseReference db, Context context) {
        instance = new FireBaseConnection(db, context);
        UserEatenFoodInADay.init(context);
    }

    public boolean isExist(String username, String password) {
        return false;
    }

    public void setValues() {

    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
