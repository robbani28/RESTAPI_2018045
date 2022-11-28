package com.kotdev.food.app.ui.adapters.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kotdev.food.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    ImageView icon;
    TextView username;
    TextView email;
    TextView website;
    TextView size_posts;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        size_posts = itemView.findViewById(R.id.size_posts);
        icon = itemView.findViewById(R.id.icon);
        username = itemView.findViewById(R.id.username);
        email = itemView.findViewById(R.id.email);
        website = itemView.findViewById(R.id.website);
    }

    public void bind(int icons, String usernames, String emails, String websites, int size) {

        RequestOptions roundedCorners =
                RequestOptions.bitmapTransform(new RoundedCorners(21));

        Glide.with(icon)
                .setDefaultRequestOptions(roundedCorners)
                .load(icons)
                .placeholder(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(icon);
        username.setText(usernames);
        email.setText(emails);
        website.setText(websites);
        size_posts.setText(String.format("Total posts %s", size));
    }
}