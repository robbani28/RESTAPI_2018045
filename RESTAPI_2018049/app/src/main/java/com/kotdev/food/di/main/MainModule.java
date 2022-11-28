package com.kotdev.food.di.main;


import androidx.recyclerview.widget.ConcatAdapter;

import com.kotdev.food.network.auth.AuthApi;
import com.kotdev.food.network.main.MainApi;
import com.kotdev.food.app.ui.adapters.CategoriesRecipeRecyclerAdapter;
import com.kotdev.food.app.ui.adapters.HeaderAdapter;
import com.kotdev.food.app.ui.adapters.ProfilePostRecyclerAdapter;
import com.kotdev.food.app.ui.adapters.RecipesRecyclerAdapter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static AuthApi provideAuthApi(@Named("Auth") Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

    @MainScope
    @Provides
    static MainApi provideMainApi(@Named("Recipes") Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

    @MainScope
    @Provides
    static CategoriesRecipeRecyclerAdapter provideCategoriesRecipeRecyclerAdapter(){
        return new CategoriesRecipeRecyclerAdapter();
    }


    @MainScope
    @Provides
    static ProfilePostRecyclerAdapter provideAdapter(){
        return new ProfilePostRecyclerAdapter();
    }

    @MainScope
    @Provides
    static RecipesRecyclerAdapter provideRecipesRecyclerAdapter(){
        return new RecipesRecyclerAdapter();
    }

    @MainScope
    @Provides
    static HeaderAdapter provideHeaderAdapter(){
        return new HeaderAdapter();
    }

    @MainScope
    @Provides
    static ConcatAdapter provideConcatAdapter(HeaderAdapter headerAdapter, ProfilePostRecyclerAdapter postRecyclerAdapter){
        return new ConcatAdapter(headerAdapter, postRecyclerAdapter);
    }
}
