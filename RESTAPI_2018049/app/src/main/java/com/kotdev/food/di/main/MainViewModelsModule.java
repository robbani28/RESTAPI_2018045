package com.kotdev.food.di.main;

import androidx.lifecycle.ViewModel;


import com.kotdev.food.di.ViewModelKey;
import com.kotdev.food.app.viewmodels.detailpost.RecipeViewModel;
import com.kotdev.food.app.viewmodels.posts.PostsViewModel;
import com.kotdev.food.app.viewmodels.profile.ProfileViewModel;
import com.kotdev.food.app.viewmodels.recipes.RecipesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)

    public abstract ViewModel bindProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel.class)
    public abstract ViewModel bindPostsViewModel(PostsViewModel postsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel.class)
    public abstract ViewModel bindRecipesViewModel(RecipesViewModel recipesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    public abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);
}
