package com.example.mybodyfit.struct.recyclerview_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.searchFood.Result;

import java.util.ArrayList;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.MyViewHolder> {

    private final ArrayList<Result> list;
    public static RecyclerViewListener listener;

    public SearchResultRecyclerViewAdapter(ArrayList<Result> list, RecyclerViewListener listener) {
        this.list = list;
        SearchResultRecyclerViewAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txt;
        public MyViewHolder(final View v) {
            super(v);
            txt = v.findViewById(R.id.result_txt);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.txt.setText(list.get(position).name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
