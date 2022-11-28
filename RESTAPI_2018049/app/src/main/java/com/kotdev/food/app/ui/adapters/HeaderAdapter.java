package com.kotdev.food.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kotdev.food.R;
import com.kotdev.food.app.ui.adapters.holder.HeaderViewHolder;

public class HeaderAdapter extends  RecyclerView.Adapter<HeaderViewHolder> {


    private int size;
    private int icons;
    private String usernames;
    private String emails;
    private String websites;

    public void setIcons(int icons) {
        this.icons = icons;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

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
        holder.bind(icons, usernames, emails, websites, size);
    }


    @Override
    public int getItemCount() {
        return 1;
    }
}