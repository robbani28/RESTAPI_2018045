package com.kotdev.food.app.viewmodels.detailpost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kotdev.food.di.main.MainScope;
import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.repositories.RecipeRepository;
import com.kotdev.food.helpers.resource.Resource;

import javax.inject.Inject;

@MainScope
public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;

    @Inject
    public RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;

    }

    public LiveData<Resource<Recipe>> searchRecipeApi(String recipeId){
        return recipeRepository.searchRecipeApi(recipeId);
    }
}
