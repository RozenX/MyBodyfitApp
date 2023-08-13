package com.example.mybodyfit.struct.recyclerview_adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.randomRecipes.ExtendedIngredient;

import java.util.ArrayList;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.MyViewHolder> {

    private final ArrayList<ExtendedIngredient> list;
    public static RecyclerViewListener listener;

    public RecipeIngredientsAdapter(ArrayList<ExtendedIngredient> list, RecyclerViewListener listener) {
        this.list = list;
        SearchResultRecyclerViewAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView amount;
        private final TextView ingredient;

        public MyViewHolder(final View v) {
            super(v);
            amount = v.findViewById(R.id.amount_of_ing);
            ingredient = v.findViewById(R.id.ingredient);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ing_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ingredient.setText(list.get(position).name);
        holder.amount.setText(Double.toString(list.get(position).amount));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
