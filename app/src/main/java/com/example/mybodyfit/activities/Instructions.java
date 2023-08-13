package com.example.mybodyfit.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
        recipeName.setText(getIntent().getStringExtra("name"));

        steps = (ArrayList<AnalyzedInstruction>) getIntent().getSerializableExtra("steps");
        ingredients = (ArrayList<ExtendedIngredient>) getIntent().getSerializableExtra("needed");

        ingredientsRecyclerView = findViewById(R.id.needed_recycler_view);
        stepsRecyclerView = findViewById(R.id.instructions_rcycler_view);
        setAdapters();
    }

    public void setAdapters() {
        RecipeIngredientsAdapter ingAdapter = new RecipeIngredientsAdapter(ingredients, (v, pos) -> {
            if (v.getId() == R.id.imageView21) {
                System.out.println("cool");
            }
        });
        ingredientsRecyclerView.setAdapter(ingAdapter);
        RecipeRecyclerViewInstructionsAdapter stepsAdapter
                = new RecipeRecyclerViewInstructionsAdapter(steps, (v, pos) -> {
            if (v.getId() == R.id.imageView20) {
                System.out.println("cool");
            }
        });
        stepsRecyclerView.setAdapter(stepsAdapter);
    }
}