package com.kotdev.food.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kotdev.food.databinding.FragmentDetalRecipeBinding;
import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.app.ui.activities.MainActivity;
import com.kotdev.food.app.viewmodels.ViewModelProviderFactory;
import com.kotdev.food.app.viewmodels.detailpost.RecipeViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class DetailRecipeFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PostsFragment";

    private RecipeViewModel viewModel;
    private FragmentDetalRecipeBinding binding;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(RecipeViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        getIncomingArguments();
        ((MainActivity) requireActivity()).searchView.setVisibility(View.GONE);
    }

    private void getIncomingArguments() {
        if (getArguments() != null) {
            Recipe recipe = getArguments().getParcelable("id");
            subscribeObservers(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers(final String recipeId) {
        viewModel.searchRecipeApi(recipeId).observe(getViewLifecycleOwner(), recipeResource -> {
            if (recipeResource != null) {
                if (recipeResource.data != null) {
                    switch (recipeResource.status) {
                        case LOADING: {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            break;
                        }
                        case SUCCESS: {
                            Log.d(TAG, "onChanged: cache has been refreshed.");
                            Log.d(TAG, "onChanged: status: SUCCESS, Recipe: " + recipeResource.data.getTitle());
                            showParent();
                            binding.progressBar.setVisibility(View.GONE);
                            setRecipeProperties(recipeResource.data);
                            binding.swipeRefreshLayout.setRefreshing(false);
                            break;
                        }
                        case ERROR: {
                            Log.e(TAG, "onChanged: status: ERROR, Recipe: " + recipeResource.data.getTitle());
                            Log.e(TAG, "onChanged: status: ERROR message: " + recipeResource.message);
                            showParent();
                            setRecipeProperties(recipeResource.data);
                            binding.progressBar.setVisibility(View.GONE);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void showParent() {
        binding.parent.setVisibility(View.VISIBLE);
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            Glide.with(requireContext())
                    .load(recipe.getImage_url())
                    .into(binding.recipeImage);
            binding.recipeTitle.setText(recipe.getTitle());
            binding.recipeSocialScore.setText(String.valueOf(Math.round(Float.parseFloat(recipe.getSocial_rank()))));
            setIngredients(recipe);
        }
    }

    private void setIngredients(Recipe recipe) {
        binding.ingredientsContainer.removeAllViews();
        if (recipe.getIngredients() != null) {
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(requireContext());
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                binding.ingredientsContainer.addView(textView);
            }
        } else {
            TextView textView = new TextView(requireContext());
            textView.setText("Error retrieving ingredients.\nCheck network connection.");
            textView.setTextSize(15);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.ingredientsContainer.addView(textView);
        }
    }

    @Override
    public void onRefresh() {
        getIncomingArguments();
    }
}