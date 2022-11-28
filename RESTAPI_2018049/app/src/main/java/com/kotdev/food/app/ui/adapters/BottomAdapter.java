package com.kotdev.food.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kotdev.food.R;
import com.kotdev.food.app.ui.adapters.holder.HeaderViewHolder;

public class BottomAdapter extends  RecyclerView.Adapter<HeaderViewHolder> {


    private int size;


    public void setSize(int size) {
        this.size = size;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return size;
    }
}