package com.kotdev.food.di.main;

import com.kotdev.food.app.ui.fragments.DetailRecipeFragment;
import com.kotdev.food.app.ui.fragments.PostsFragment;
import com.kotdev.food.app.ui.fragments.ProfileFragment;
import com.kotdev.food.app.ui.fragments.RecipesListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {


    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();


    @ContributesAndroidInjector
    abstract PostsFragment contributePostsFragment();


    @ContributesAndroidInjector
    abstract RecipesListFragment contributeRecipesListFragment();

    @ContributesAndroidInjector
    abstract DetailRecipeFragment contributeDetalRecipeFragment();
}
