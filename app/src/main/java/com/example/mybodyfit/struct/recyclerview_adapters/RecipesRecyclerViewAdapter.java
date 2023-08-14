package com.example.mybodyfit.struct.recyclerview_adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.randomRecipes.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.MyViewRecipesViewHolder> {

    private final ArrayList<Recipe> list;
    public static RecyclerViewListener listener;

    public RecipesRecyclerViewAdapter(ArrayList<Recipe> list, RecyclerViewListener listener) {
        this.list = list;
        RecipesRecyclerViewAdapter.listener = listener;
    }

    public static class MyViewRecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dishName;
        private final ImageView foodImage;

        public MyViewRecipesViewHolder(final View v) {
            super(v);
            dishName = v.findViewById(R.id.dish_name);
            foodImage = v.findViewById(R.id.dishImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recpies_item_view, parent, false);
        return new MyViewRecipesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull MyViewRecipesViewHolder holder, int position) {
        String title = list.get(position).title;
        holder.dishName.setText(title);
        Picasso.get().load(list.get(position).image).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
