package com.example.mybodyfit.struct.recyclerview_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.searchFood.Result;

import java.util.List;

public class ItemViewRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewRecyclerViewAdapter.MyViewHolder> {

    private final List<Result> list;
    public static RecyclerViewListener listener;

    public ItemViewRecyclerViewAdapter(List<Result> list, RecyclerViewListener listener) {
        this.list = list;
        SettingsRecyclerViewAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView amount;
        private final TextView calories;

        public MyViewHolder(final View v) {
            super(v);
            name = v.findViewById(R.id.food_name_view);
            amount = v.findViewById(R.id.amount);
            calories = v.findViewById(R.id.calories_view);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).name);
//        holder.amount.setText(list.get(position).unit);
//        holder.calories.setText(AppConstants.NutrientBreakDownCalculations.getCalories(
//                list.get(position).nutrition.caloricBreakdown.percentProtein,
//                list.get(position).nutrition.caloricBreakdown.percentCarbs,
//                list.get(position).nutrition.caloricBreakdown.percentFat,
//                list.get(position).nutrition.weightPerServing.amount));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
