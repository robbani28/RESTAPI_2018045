package com.kotdev.food.helpers.util;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kotdev.food.app.models.Category;
import com.kotdev.food.helpers.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class PostsGeneration {

    private static final MutableLiveData<Resource<List<Category>>> liveData = new MutableLiveData<>();

    @SuppressLint("DefaultLocale")
    public static LiveData<Resource<List<Category>>> getRecipeCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Category recipe = new Category();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            float random = (float) (4 + Math.random() * (5 - 4));
            recipe.setSocial_rank(String.format("%.1f",random));
            categories.add(recipe);
        }
        liveData.setValue(Resource.success(categories));
        return liveData;
    }

}
