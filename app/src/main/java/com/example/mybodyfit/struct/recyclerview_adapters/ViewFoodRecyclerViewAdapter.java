package com.example.mybodyfit.struct.recyclerview_adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.dataBase.entities.Foods;
import com.example.mybodyfit.struct.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class ViewFoodRecyclerViewAdapter extends RecyclerView.Adapter<ViewFoodRecyclerViewAdapter.MyViewFoodViewHolder> {

    private List<Foods> list = new ArrayList<>();
    public static RecyclerViewListener listener;

    public ViewFoodRecyclerViewAdapter(RecyclerViewListener listener) {
        ViewFoodRecyclerViewAdapter.listener = listener;
        list = new ArrayList<>();
    }

    public static class MyViewFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView foodName;
        private final TextView amount;
        private final TextView scalingUnit;
        private final TextView calories;
        private final Button deleteBtn;

        public MyViewFoodViewHolder(final View v) {
            super(v);
            foodName = v.findViewById(R.id.food_name_view);
            amount = v.findViewById(R.id.amount);
            scalingUnit = v.findViewById(R.id.scaling_unit);
            calories = v.findViewById(R.id.calories_view);
            deleteBtn = v.findViewById(R.id.delete_btn);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewFoodViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewFoodViewHolder holder, int position) {
        String name = list.get(position).getFoodName();
        String calories = Double.toString(list.get(position).getCalories());
        String amount = Double.toString(list.get(position).getAmount());

        holder.foodName.setText(name);
        holder.calories.setText(calories);
        holder.amount.setText(amount);
        holder.deleteBtn.setOnClickListener(v -> {
            listener.onClick(v, position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFoods(List<Foods> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Foods> getList() {
        return list;
    }
}

