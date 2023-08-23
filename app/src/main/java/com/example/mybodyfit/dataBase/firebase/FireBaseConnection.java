package com.example.mybodyfit.dataBase.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.UserEatenFoodInADay;
import com.example.mybodyfit.dataBase.dao.FoodDao;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.dataBase.viewModels.FoodViewModel;
import com.example.mybodyfit.struct.UserName;
import com.example.mybodyfit.struct.UsersFoods;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

public final class FireBaseConnection {

    UserEatenFoodInADay foodDb;
    private final DatabaseReference reference;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    FirebaseAuth firebaseAuth;
    private static FireBaseConnection instance = null;

    @Singleton
    public static FireBaseConnection getInstance() {
        return instance;
    }

    private FireBaseConnection(Context context) {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.context = context;

        foodDb = UserEatenFoodInADay.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static void init(Context context) {
        instance = new FireBaseConnection(context);
        UserEatenFoodInADay.init(context);
    }

    public void addUserFoods(MyBodyDatabase db, LifecycleOwner activity) {
        db.foodDao().pullAll().observe(activity, foods -> {
            reference.child("foodsDb").child(UserName
                            .getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    .child("foods").setValue(foods).addOnCompleteListener(task -> {
                        if (!task.isSuccessful())
                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "food was added",
                                    Toast.LENGTH_SHORT).show();
                    });
            firebaseAuth.signOut();
        });
    }

    public void addFoodsByUser(FoodDao foodDao) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Runnable runnable = () -> {
                    GenericTypeIndicator<ArrayList<Foods>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Foods>>() {
                    };
                    ArrayList<Foods> foods = snapshot.child("foodsDb").child(UserName
                                    .getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            .child("foods").getValue(genericTypeIndicator);
                    if (foods != null)
                        for (Foods f : foods) {
                            foodDao.insert(f);
                        }
                };
                new Thread(runnable).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
