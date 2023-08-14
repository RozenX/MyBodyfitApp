package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.mybodyfit.struct.recyclerview_adapters.RecipesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Recipes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);
        bnv = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        ProgressHelper.showDialog(this, "loading...");

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
                Bundle bundle = new Bundle();
                bundle.putString("recipeName", response.recipes.get(pos).sourceName);
                bundle.putSerializable("steps", response.recipes.get(pos).analyzedInstructions);
                bundle.putSerializable("needed", response.recipes.get(pos).extendedIngredients);
                Toast.makeText(Recipes.this, "i was pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Instructions.class));
            }));
            ProgressHelper.setDialogToSleep(true);
        }

        @Override
        public void didError(String msg) {

        }
    };
}