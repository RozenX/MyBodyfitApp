package com.example.mybodyfit.struct.recyclerview_adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.Attributes;

import java.util.List;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.MyViewHolder> {

    private final List<? extends Attributes> list;
    public static RecyclerViewListener listener;

    public SettingsRecyclerViewAdapter(List<? extends Attributes> list, RecyclerViewListener listener) {
        this.list = list;
        SettingsRecyclerViewAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txt;
        private final ImageView img;

        public MyViewHolder(final View v) {
            super(v);
            txt = v.findViewById(R.id.setting_type);
            img = v.findViewById(R.id.imageView19);
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
        String type = list.get(position).getType();
        if (list.get(position).getDrawable() != null) {
            holder.img.setImageResource(list.get(position).getDrawable());
        } else {
            holder.img.setImageResource(R.drawable.transperent_background);
        }
        holder.txt.setText(type);
        if (holder.txt.getText().toString().equals("Log Out")) {
            holder.txt.setTextColor(Color.parseColor("#d46666"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
