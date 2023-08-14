package com.example.mybodyfit.struct.recyclerview_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybody.R;
import com.example.mybodyfit.struct.models.randomRecipes.AnalyzedInstruction;

import java.util.ArrayList;

public class RecipeRecyclerViewInstructionsAdapter extends RecyclerView.Adapter<RecipeRecyclerViewInstructionsAdapter.MyViewHolder> {

    private final ArrayList<AnalyzedInstruction> list;
    public static RecyclerViewListener listener;

    public RecipeRecyclerViewInstructionsAdapter(ArrayList<AnalyzedInstruction> list, RecyclerViewListener listener) {
        this.list = list;
        RecipeRecyclerViewInstructionsAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView step;
        private final TextView instruction;

        public MyViewHolder(final View v) {
            super(v);
            step = v.findViewById(R.id.current_step);
            instruction = v.findViewById(R.id.instructions_txt);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructions_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.step.setText(list.get(position).steps.get(position).number);
        holder.instruction.setText(list.get(position).steps.get(position).step);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
