package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.constants.AppConstants;
import com.example.mybodyfit.dataBase.UserEatenFoodInADay;
import com.example.mybodyfit.spoonacular.RequestManger;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.FoodViewAttributes;
import com.example.mybodyfit.struct.Listeners.FoodNameResponseListener;
import com.example.mybodyfit.struct.Listeners.FoodNutrientsResponseListener;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.ProgressHelper;
import com.example.mybodyfit.struct.models.nutrients.FoodNutrientsApiResponseSP;
import com.example.mybodyfit.struct.models.nutrients.Nutrient;
import com.example.mybodyfit.struct.models.searchFood.Result;
import com.example.mybodyfit.struct.models.searchFood.SearchFoodApiResponse;
import com.example.mybodyfit.struct.recyclerview_adapters.SearchResultRecyclerViewAdapter;
import com.example.mybodyfit.struct.recyclerview_adapters.ViewFoodRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ViewFoodEatenByTime extends AppCompatActivity {

    static SearchFoodApiResponse response;
    static boolean toSearch = false;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private Spinner meals;
    private RecyclerView recyclerView;
    private ArrayList<FoodViewAttributes> foods;
    private SearchView searchEngine;
    boolean didFetch = false;
    private ArrayList<Result> res;
    private Bundle bundle;
    private String foodName;
    private Intent in;
    private ArrayList<FoodModel> foodStats;
    private ArrayList<FoodModel> relevant;
    private RecyclerView searchRecyclerView;
    private RequestManger manger;
    private UserEatenFoodInADay db;
    int mealTime;
    private ArrayAdapter<CharSequence> adapter;
    String mealTimeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_eaten_by_time);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        UserEatenFoodInADay.init(this);
        db = UserEatenFoodInADay.getInstance();

        foods = new ArrayList<>();
        relevant = new ArrayList<>();
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.log);
        fab = findViewById(R.id.fab);
        meals = findViewById(R.id.type_of_meal);
        recyclerView = findViewById(R.id.food_eaten_recycler_view);
        searchEngine = findViewById(R.id.search_engine);
        manger = new RequestManger(this);
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        in = new Intent(ViewFoodEatenByTime.this, ViewFoodStats.class);
        bundle = new Bundle();
        searchEngine.setIconified(false);
        searchEngine.clearFocus();

        search();
        if (getIntent().getStringExtra("from") != null) {
            mealTime = Integer.parseInt(getIntent().getStringExtra("mealTime"));
            mealTimeTxt = getIntent().getStringExtra("mealTimeInText");
        } else {
            addLogs();
        }
        adapter = new ArrayAdapter<>(ViewFoodEatenByTime.this,
                android.R.layout.simple_spinner_item,
                AppConstants.FoodViewSpinnerListConst.getListAdapter());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meals.setAdapter(adapter);
        foodStats = new ArrayList<>(db.pullFoods(mealTime));
        showPopUp();
        setAdapter();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void setAdapter() {
        ViewFoodRecyclerViewAdapter adapter = new ViewFoodRecyclerViewAdapter(foodStats, ((v, position) -> {
           popUpDeleteFood(v, foodStats.get(position).getName());
        }));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void addLogs() {
        String time = getIntent().getStringExtra("meal time");
        mealTime = getIntent().getIntExtra("meal", 0);
    }

    public void search() {
        searchEngine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    toSearch = true;
                    manger.getFoods(listener, query);
                } else {
                    toSearch = false;
                    manger.getFoods(listener, "ליאור");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchEngine.isIconified()) {
                    toSearch = false;
                    manger.getFoods(listener, "ליאור");
                } else {
                    toSearch = true;
                    manger.getFoods(listener, newText);
                }
                return false;
            }
        });
        searchEngine.setOnCloseListener(() -> {
            manger.getFoods(listener, "ליאור");
            return false;
        });
    }

    final FoodNutrientsResponseListener nutrientsListener = new FoodNutrientsResponseListener() {
        @Override
        public void didFetch(FoodNutrientsApiResponseSP response, String msg) {
            setFoodName(response.name);
            if (foodName.equals(response.name)) {
                bundle.putString("name", foodName);
                if (response.nutrition != null) {
                    for (Nutrient nut : response.nutrition.nutrients) {
                        System.out.println(nut.name);
                        if (nut.name.equals("Calories")) {
                            bundle.putString("calories", Double.toString(nut.amount));
                        }
                        if (nut.name.equals("Protein")) {
                            bundle.putString("protein", Double.toString(nut.amount));
                        }
                        if (nut.name.equals("Carbohydrates")) {
                            bundle.putString("carbs", Double.toString(nut.amount));
                        }
                        if (nut.name.equals("Fat")) {
                            bundle.putString("fats", Double.toString(nut.amount));
                        }
                    }
                }
            } else {
                Toast.makeText(ViewFoodEatenByTime.this, "I AM NULL", Toast.LENGTH_SHORT).show();
            }
            didFetch = true;
            goToViewFood();
        }

        @Override
        public void didError(String msg) {
            Toast.makeText(ViewFoodEatenByTime.this, "did something bad", Toast.LENGTH_SHORT).show();
        }
    };

    public void setSearchAdapter() {
        res = response.results;
        searchRecyclerView.setAdapter(new SearchResultRecyclerViewAdapter(res, (view, pos) -> {
            ProgressHelper.showDialog(ViewFoodEatenByTime.this, "wait...");
            manger.getFoodNutrients(nutrientsListener, Integer.toString(res.get(pos).id));
            ProgressHelper.setDialogToSleep(true);
        }));
    }

    public void goToViewFood() {
        if (didFetch && ProgressHelper.isAsleep()) {
            if (!this.isEmpty(foodName)) {
                //  Toast.makeText(this, imgUrl, Toast.LENGTH_SHORT).show();
            }
            in.putExtras(bundle);
            startActivity(in);
        }
    }

    final FoodNameResponseListener listener = new FoodNameResponseListener() {
        @Override
        public void didFetch(SearchFoodApiResponse response, String msg) {
            searchRecyclerView.setHasFixedSize(true);
            searchRecyclerView.setLayoutManager(new GridLayoutManager(ViewFoodEatenByTime.this, 1));
            searchRecyclerView.setVisibility(View.VISIBLE);
            if (toSearch) {
                ViewFoodEatenByTime.response = response;
                setSearchAdapter();
            }
            if (response.results.isEmpty()) {
                searchRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void didError(String msg) {

        }
    };


    public boolean isEmpty(String response) {
        return response.equals("") || response == null;
    }

    public void setFoodName(String name) {
        if (!name.equals("")) {
            foodName = name;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void manageNavigation() {
        fab.setOnClickListener(v -> goToAddFood());
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    gotToHome();
                    return true;
                case R.id.settings:
                    goToSettings();
                    return true;
                case R.id.recipes:
                    goToRecipes();
                    return true;
            }
            return false;
        });
    }

    public void gotToHome() {
        Intent intent = new Intent(ViewFoodEatenByTime.this, Home.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToSettings() {
        Intent intent = new Intent(ViewFoodEatenByTime.this, Settings.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToAddFood() {
        Intent intent = new Intent(ViewFoodEatenByTime.this, AddFoods.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    public void goToRecipes() {
        Intent intent = new Intent(ViewFoodEatenByTime.this, Recipes.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toSearch = false;
        manger.getFoods(listener, "ליאור");
    }

    @SuppressLint("SetTextI18n")
    public void showPopUp() {
        meals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String meal = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                if (meal.equals("Breakfast")) {
                    mealTime = FoodModel.BREAKFAST;
                    foodStats.clear();
                    foodStats = new ArrayList<>(db.pullFoods(mealTime));
                    setAdapter();
                }
                if (meal.equals("Lunch")) {
                    mealTime = FoodModel.LUNCH;
                    foodStats.clear();
                    foodStats = new ArrayList<>(db.pullFoods(mealTime));
                    setAdapter();
                }
                if (meal.equals("Dinner")) {
                    mealTime = FoodModel.DINNER;
                    foodStats.clear();
                    foodStats = new ArrayList<>(db.pullFoods(mealTime));
                    setAdapter();
                }
                if (meal.equals("Snacks")) {
                    mealTime = FoodModel.SNACK;
                    foodStats.clear();
                    foodStats = new ArrayList<>(db.pullFoods(mealTime));
                    setAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void popUpDeleteFood(View v, String foodToDelete) {
        PopupMenu popUp = new PopupMenu(ViewFoodEatenByTime.this, v);
        popUp.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                db.deleteFood(foodToDelete);
                foodStats.clear();
                foodStats = new ArrayList<>(db.pullFoods(mealTime));
                setAdapter();
                return true;
            }
            return false;
        });
        popUp.inflate(R.menu.pop_up_delete);
        popUp.show();
    }
}
