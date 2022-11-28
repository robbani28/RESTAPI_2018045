package com.kotdev.food.app.ui.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kotdev.food.R;
import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.interfaces.ClickCallback;
import com.kotdev.food.app.ui.adapters.holder.LoadingViewHolder;
import com.kotdev.food.app.ui.adapters.holder.SearchExhaustedViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    private List<Recipe> posts;
    private ClickCallback<Recipe> clickCallback;

    public void setClickCallback(ClickCallback<Recipe> clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case RECIPE_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new PostViewHolder(view);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new PostViewHolder(view);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            ((PostViewHolder) holder).bind(position, posts.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(posts != null){
            return posts.size();
        }
        return 0;
    }

    public void setPosts(List<Recipe> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void setQueryExhausted() {
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        posts.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (posts.get(position).getTitle().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    public void displayOnlyLoading() {
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setTitle("LOADING...");
        posts.add(recipe);
        notifyDataSetChanged();
    }

    private void clearRecipesList() {
        if (posts == null) {
            posts = new ArrayList<>();
        } else {
            posts.clear();
        }
        notifyDataSetChanged();
    }
    public void displayLoading(){
        if(posts == null){
            posts = new ArrayList<>();
        }
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            posts.add(recipe);
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if(isLoading()) {
            if (posts.get(0).getTitle().equals("LOADING...")) {
                posts.remove(posts.size() - 1);
            }
        }
        if(isLoading()){
            if(posts.get(posts.size() - 1).getTitle().equals("LOADING...")){
                posts.remove(posts.size() - 1);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if (posts.size() > 0) {
            return posts.get(posts.size() - 1).getTitle().equals("LOADING...");
        }
        return false;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView title, publisher, socialScore;
        AppCompatImageView image;
        ConstraintLayout main_l;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            main_l = itemView.findViewById(R.id.main_l);
            title = itemView.findViewById(R.id.recipe_title);
            publisher = itemView.findViewById(R.id.recipe_publisher);
            socialScore = itemView.findViewById(R.id.recipe_social_score);
            image = itemView.findViewById(R.id.recipe_image);
        }

        public void bind(int position, Recipe recipe) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Glide.with(image)
                    .setDefaultRequestOptions(options)
                    .load(recipe.getImage_url())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(image);
            main_l.setOnClickListener(v -> clickCallback.clickListener(position, posts.get(position)));
            title.setText(posts.get(position).getTitle());
            publisher.setText(posts.get(position).getPublisher());
            socialScore.setText(String.format("%.1f", Float.parseFloat(posts.get(position).getSocial_rank()) / 20));
        }
    }
}