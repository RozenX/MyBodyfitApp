package com.example.mybodyfit.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.randomRecipes.AnalyzedInstruction;
import com.example.mybodyfit.struct.models.randomRecipes.ExtendedIngredient;
import com.example.mybodyfit.struct.recyclerview_adapters.RecipeIngredientsAdapter;
import com.example.mybodyfit.struct.recyclerview_adapters.RecipeRecyclerViewInstructionsAdapter;

import java.util.ArrayList;

public class Instructions extends AppCompatActivity {

    private TextView recipeName;
    private RecyclerView ingredientsRecyclerView;
    private RecyclerView stepsRecyclerView;
    private ArrayList<AnalyzedInstruction> steps;
    private ArrayList<ExtendedIngredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        recipeName = findViewById(R.id.recipe_name);
        if (getIntent().getExtras() != null) {
            recipeName.setText(getIntent().getStringExtra("recipeName"));
            steps = new ArrayList<>((ArrayList<AnalyzedInstruction>) getIntent().getSerializableExtra("steps"));
            ingredients = new ArrayList<>( (ArrayList<ExtendedIngredient>) getIntent().getSerializableExtra("needed"));
        } else {
            Toast.makeText(this, "it is null tho", Toast.LENGTH_SHORT).show();
        }
        ingredientsRecyclerView = findViewById(R.id.needed_recycler_view);
        stepsRecyclerView = findViewById(R.id.instructions_rcycler_view);
        setAdapters();
    }

    public void setAdapters() {

        RecipeIngredientsAdapter ingAdapter = new RecipeIngredientsAdapter(ingredients, (v, pos) -> {
                System.out.println("cool");
        });
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        ingredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientsRecyclerView.setAdapter(ingAdapter);
        RecipeRecyclerViewInstructionsAdapter stepsAdapter
                = new RecipeRecyclerViewInstructionsAdapter(steps, (v, pos) -> {
                System.out.println("cool");
        });
        stepsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setAdapter(stepsAdapter);
    }
}