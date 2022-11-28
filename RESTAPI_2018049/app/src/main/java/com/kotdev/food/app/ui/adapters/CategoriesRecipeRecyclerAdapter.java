package com.kotdev.food.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kotdev.food.R;
import com.kotdev.food.app.models.Category;
import com.kotdev.food.interfaces.ClickCallback;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRecipeRecyclerAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> posts = new ArrayList<>();


    private ClickCallback<Category> clickCallback;

    public void setClickCallback(ClickCallback<Category> clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public CategoriesRecipeRecyclerAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CategoriesRecipeRecyclerAdapter.PostViewHolder)holder).bind(position, posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Category> posts) {
        this.posts = posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        TextView category_rank;
        TextView category_title;
        TextView category_posts;
        ImageView category_image;
        ConstraintLayout cardView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            category_rank = itemView.findViewById(R.id.category_rank);
            category_title = itemView.findViewById(R.id.category_title);
            category_posts = itemView.findViewById(R.id.category_posts);
            category_image = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.CardView);
        }

        public void bind(int position, Category post){
            category_title.setText(post.getTitle());
            category_posts.setText(String.format("%s recipes", "test"));
            category_rank.setText(String.valueOf(post.getSocial_rank()));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);


            Glide.with(category_image)
                    .setDefaultRequestOptions(options)
                    .load(post.getImage_url())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(category_image);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallback.clickListener(position, post);
                }
            });
        }
    }
}