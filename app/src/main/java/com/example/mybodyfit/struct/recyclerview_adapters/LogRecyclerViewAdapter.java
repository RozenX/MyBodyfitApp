package com.example.mybodyfit.struct.recyclerview_adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.Attributes;

import java.util.List;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.MyLogViewHolder> {

    private static int sum = 0;
    private final List<? extends Attributes> list;
    private int[] cal;
    public static RecyclerViewListener listener;

    public LogRecyclerViewAdapter(List<? extends Attributes> list, int[] calories, RecyclerViewListener listener) {
        this.list = list;
        cal = calories;
        LogRecyclerViewAdapter.listener = listener;
    }

    public static class MyLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txt;
        private final TextView add_txt;
        private TextView calories;
        private final ImageButton btn;
        private final ImageButton toView;

        public MyLogViewHolder(final View v) {
            super(v);
            txt = v.findViewById(R.id.meal_time);
            btn = v.findViewById(R.id.log_popup_btn);
            add_txt = v.findViewById(R.id.add_txt);
            toView = v.findViewById(R.id.to_view_btn);
            calories = v.findViewById(R.id.calories_log);
            toView.setOnClickListener(this);
            add_txt.setTextColor(Color.parseColor("#0f6fe3"));
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new MyLogViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyLogViewHolder holder, int position) {
        String type = list.get(position).getType();
        holder.txt.setText(type);
        switch (type) {
            case "Breakfast":
                holder.calories.setText(Integer.toString(cal[0]));
                break;
            case "Lunch":
                holder.calories.setText(Integer.toString(cal[1]));
                break;
            case "Dinner":
                holder.calories.setText(Integer.toString(cal[2]));
                break;
            case "Snacks":
                holder.calories.setText(Integer.toString(cal[3]));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

