package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;

import com.example.mybodyfit.spoonacular.RequestManger;
import com.example.mybodyfit.struct.Listeners.RandomRecipeResponseListener;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.ProgressHelper;
import com.example.mybodyfit.struct.models.randomRecipes.RandomRecipeApiResponse;
import com.example.mybodyfit.struct.models.randomRecipes.Recipe;
import com.example.mybodyfit.struct.recyclerview_adapters.RecipesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private ArrayList<Recipe> rec;
    private Intent intent;
    private boolean isPressed = false;
    private boolean didReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);
        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        ProgressHelper.showDialog(this, "loading...");
        intent = new Intent(getApplicationContext(), Instructions.class);
        RequestManger manger = new RequestManger(this);
        manger.getRandomRecipes(listener);

        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        fab.setOnClickListener(v -> goToAddFood());
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    goToHome();
                    return true;
                case R.id.settings:
                    goToSettings();
                    return true;
                case R.id.log:
                    goToLog();
                    return true;
            }
            return false;
        });
    }

    public void goToHome() {
        Intent intent = new Intent(Recipes.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToLog() {
        Intent intent = new Intent(Recipes.this, Log.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }


    public void goToSettings() {
        Intent intent = new Intent(Recipes.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(Recipes.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    final RandomRecipeResponseListener listener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String msg) {
            recyclerView = findViewById(R.id.recipes_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(Recipes.this, 1));
            recyclerView.setAdapter(new RecipesRecyclerViewAdapter(response.recipes, (view, pos) -> {
                isPressed = true;
                rec = response.recipes;
                Bundle bundle = new Bundle();
                if (isReady(pos)) {
                    bundle.putString("recipeName", response.recipes.get(pos).title);
                    bundle.putSerializable("steps", (Serializable) response.recipes.get(pos).analyzedInstructions);
                    bundle.putSerializable("needed", (Serializable) response.recipes.get(pos).extendedIngredients);
                    intent.putExtras(bundle);
                    didReady = true;
                    goToInstructions();
                }
            }));
            ProgressHelper.setDialogToSleep(true);
        }

        public void goToInstructions() {
            if (didReady) {
                startActivity(intent);
            }
        }

        @Override
        public void didError(String msg) {
        }
    };

    public boolean isReady(int pos) {
        return !rec.get(pos).sourceName.equals("") && rec.get(pos).analyzedInstructions != null && rec.get(pos).extendedIngredients != null;
    }
}