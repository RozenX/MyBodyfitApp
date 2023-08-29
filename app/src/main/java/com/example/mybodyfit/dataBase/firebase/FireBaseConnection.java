package com.example.mybodyfit.dataBase.firebase;

import static com.example.mybodyfit.struct.UserName.getName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mybodyfit.dataBase.MyBodyDatabase;
import com.example.mybodyfit.dataBase.dao.FoodDao;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Singleton;

public final class FireBaseConnection {

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

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static void init(Context context) {
        instance = new FireBaseConnection(context);
    }

    public void addUserFoods(MyBodyDatabase db) {
        Runnable runnable = () -> {
            AtomicBoolean isFinished = new AtomicBoolean(false);
            ArrayList<Foods> foods = new ArrayList<>(db.foodDao().pullAllData());
            for (Foods f : foods) {
                reference.child("foodsDb").child(getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        .child("foods").child(f.getFoodName()).setValue(f).addOnCompleteListener(task -> {
                            if (!task.isSuccessful())
                                Toast.makeText(context, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            else {
                                isFinished.set(true);
                            }
                        });
            }
        };
        new Thread(runnable).start();
    }

    public void addFoodsByUser(FoodDao foodDao) {
        reference.child("foodsDb").child(getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("foods").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Runnable runnable = () -> {
                            ArrayList<Foods> foods = new ArrayList<>();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                foods.add(data.getValue(Foods.class));
                            }
                            if (!foods.isEmpty()) {
                                for (Foods f : foods) {
                                    foodDao.insert(f);
                                }
                            }
                        };
                        new Thread(runnable).start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void removeFood(Foods food) {
        reference.child("foodsDb").child(getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("foods").child(food.getFoodName()).removeValue();
    }
}
