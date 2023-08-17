package com.example.mybodyfit.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.api.ApiManger;
import com.example.mybodyfit.constants.AppConstants;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.dataBase.viewModels.FoodViewModel;
import com.example.mybodyfit.struct.CurrentDate;
import com.example.mybodyfit.struct.FoodModel;
import com.example.mybodyfit.struct.Listeners.FdcFoodSearchResponseListener;
import com.example.mybodyfit.struct.Listeners.FdcNutrientsResponseListener;
import com.example.mybodyfit.struct.MenuThread;
import com.example.mybodyfit.struct.ProgressHelper;
import com.example.mybodyfit.struct.models.nutrientsFDC.FdcFoodNutrientsApiResponse;
import com.example.mybodyfit.struct.models.searchFoodFDC.FdcSearchApiResponse;
import com.example.mybodyfit.struct.models.searchFoodFDC.Food;
import com.example.mybodyfit.struct.recyclerview_adapters.SearchResultRecyclerViewAdapter;
import com.example.mybodyfit.struct.recyclerview_adapters.ViewFoodRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ViewFoodEatenByTime extends AppCompatActivity {

    static FdcSearchApiResponse response;
    static boolean toSearch = false;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private Spinner meals;
    private RecyclerView recyclerView;
    private SearchView searchEngine;
    boolean didFetch = false;
    private ArrayList<Food> res;
    private Bundle bundle;
    private String foodName;
    private Intent in;
    private RecyclerView searchRecyclerView;
    private ApiManger manger;
    private FoodViewModel viewModel;
    int mealTime;
    private ArrayAdapter<CharSequence> adapter;
    private ViewFoodRecyclerViewAdapter foodsAdapter;
    String mealTimeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_eaten_by_time);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#121212"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        viewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.log);
        fab = findViewById(R.id.fab);
        meals = findViewById(R.id.type_of_meal);
        recyclerView = findViewById(R.id.food_eaten_recycler_view);
        searchEngine = findViewById(R.id.search_engine);
        manger = new ApiManger(ViewFoodEatenByTime.this);
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
                AppConstants.FoodViewSpinnerListConst.getListAdapter(mealTime));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meals.setAdapter(adapter);
        foodsAdapter = new ViewFoodRecyclerViewAdapter(((v, position) -> {
            popUpDeleteFood(v, foodsAdapter.getList().get(position));
        }));

        viewModel.pullByMealAndDate(mealTime, CurrentDate.getDateWithoutTimeUsingCalendar()).observe(this, foods -> {
            setAdapter();
            foodsAdapter.setFoods(foods);
        });
        showPopUp();
        MenuThread.init(this::manageNavigation);
        MenuThread.getInstance().start();
    }

    public void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodsAdapter);
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
                    manger.searchFoods(listener, query);
                } else {
                    toSearch = false;
                    manger.searchFoods(listener, "ליאור");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchEngine.isIconified()) {
                    toSearch = false;
                    manger.searchFoods(listener, "ליאור");
                } else {
                    toSearch = true;
                    manger.searchFoods(listener, newText);
                }
                return false;
            }
        });
        searchEngine.setOnCloseListener(() -> {
            manger.searchFoods(listener, "ליאור");
            return false;
        });
    }


    public static class GetNut extends AsyncTask<Void, Void, FdcNutrientsResponseListener> {

        @SuppressLint("StaticFieldLeak")
        private final ViewFoodEatenByTime viewFoodEatenByTime;
        private FdcNutrientsResponseListener listener;

        public GetNut(ViewFoodEatenByTime viewFoodEatenByTime) {
            this.viewFoodEatenByTime = viewFoodEatenByTime;
        }

        @Override
        protected FdcNutrientsResponseListener doInBackground(Void... voids) {
            listener = new FdcNutrientsResponseListener() {
                @Override
                public void didFetch(FdcFoodNutrientsApiResponse response, String msg) {
                    viewFoodEatenByTime.setFoodName(response.description);
                    if (viewFoodEatenByTime.foodName.equals(response.description)) {
                        viewFoodEatenByTime.bundle.putString("name", viewFoodEatenByTime.foodName);
                        if (response.labelNutrients != null) {
                            viewFoodEatenByTime.bundle.putString("calories", Double.toString(response.labelNutrients.calories.value));
                            viewFoodEatenByTime.bundle.putString("protein", Double.toString(response.labelNutrients.protein.value));
                            viewFoodEatenByTime.bundle.putString("carbs", Double.toString(response.labelNutrients.carbohydrates.value));
                            viewFoodEatenByTime.bundle.putString("fats", Double.toString(response.labelNutrients.fat.value));
                        }
                        viewFoodEatenByTime.didFetch = true;
                    }
                    viewFoodEatenByTime.goToViewFood();
                }

                @Override
                public void didError(String msg) {

                }
            };
            return listener;
        }
    }


    public void setSearchAdapter() {
        res = response.foods;
        searchRecyclerView.setAdapter(new SearchResultRecyclerViewAdapter(res, (view, pos) -> {
            ProgressHelper.showDialog(ViewFoodEatenByTime.this, "wait...");
            try {
                manger.getFoodsNutrients(new GetNut(this).execute().get(), res.get(pos).fdcId);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
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

    final FdcFoodSearchResponseListener listener = new FdcFoodSearchResponseListener() {

        @Override
        public void didFetch(FdcSearchApiResponse response, String msg) {
            ui(response);
        }

        @Override
        public void didError(String msg) {

        }
    };

    public void ui(FdcSearchApiResponse response) {
        runOnUiThread(() -> {
            searchRecyclerView.setHasFixedSize(true);
            searchRecyclerView.setLayoutManager(new GridLayoutManager(ViewFoodEatenByTime.this, 1));
            searchRecyclerView.setVisibility(View.VISIBLE);
            if (toSearch) {
                ViewFoodEatenByTime.response = response;
                setSearchAdapter();
            }
            if (response.foods.isEmpty()) {
                searchRecyclerView.setVisibility(View.GONE);
            }
        });
    }


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
        manger.searchFoods(listener, "ליאור");
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
                    viewModel.pullByMealAndDate(mealTime, CurrentDate.getDateWithoutTimeUsingCalendar()).observe(ViewFoodEatenByTime.this, foods -> {
                        foodsAdapter.setFoods(foods);
                    });
                    setAdapter();
                }
                if (meal.equals("Lunch")) {
                    mealTime = FoodModel.LUNCH;
                    viewModel.pullByMealAndDate(mealTime, CurrentDate.getDateWithoutTimeUsingCalendar()).observe(ViewFoodEatenByTime.this, foods -> {
                        foodsAdapter.setFoods(foods);
                    });
                    setAdapter();
                }
                if (meal.equals("Dinner")) {
                    mealTime = FoodModel.DINNER;
                    viewModel.pullByMealAndDate(mealTime, CurrentDate.getDateWithoutTimeUsingCalendar()).observe(ViewFoodEatenByTime.this, foods -> {
                        foodsAdapter.setFoods(foods);
                    });

                }
                if (meal.equals("Snacks")) {
                    mealTime = FoodModel.SNACK;
                    viewModel.pullByMealAndDate(mealTime, CurrentDate.getDateWithoutTimeUsingCalendar()).observe(ViewFoodEatenByTime.this, foods -> {
                        foodsAdapter.setFoods(foods);
                    });
                    setAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void popUpDeleteFood(View v, Foods food) {
        PopupMenu popUp = new PopupMenu(ViewFoodEatenByTime.this, v);
        popUp.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                viewModel.deleteByMeal(food);
                setAdapter();
                return true;
            }
            return false;
        });
        popUp.inflate(R.menu.pop_up_delete);
        popUp.show();
    }
}
