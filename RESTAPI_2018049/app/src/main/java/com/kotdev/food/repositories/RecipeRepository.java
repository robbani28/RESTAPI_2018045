package com.kotdev.food.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.kotdev.food.di.main.MainScope;
import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.app.models.RecipeResponses;
import com.kotdev.food.app.models.RecipeSearchResponse;
import com.kotdev.food.network.main.MainApi;
import com.kotdev.food.helpers.responses.ApiResponse;
import com.kotdev.food.room.Database;
import com.kotdev.food.helpers.resource.Resource;
import com.kotdev.food.helpers.util.Constants;
import com.kotdev.food.helpers.resource.NetworkBoundResource;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

@MainScope
public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private final AppExecutors appExecutors;
    private final Database recipeDao;
    private final MainApi mainApi;

    @Inject
    RecipeRepository(AppExecutors appExecutors, Database recipeDao, MainApi mainApi) {
        this.appExecutors = appExecutors;
        this.recipeDao = recipeDao;
        this.mainApi = mainApi;
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(appExecutors) {

            @Override
            public void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    int index = 0;
                    for (long rowId : recipeDao.getDatabase().recipeDao().insertRecipes((item.getRecipes().toArray(recipes)))) {
                        if (rowId == -1) { // conflict detected
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in cache.");
                            recipeDao.getDatabase().recipeDao().updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            public boolean shouldFetch(@Nullable List<Recipe> data) {
                return true; // always query the network since the queries can be anything
            }

            @NonNull
            @Override
            public LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getDatabase().recipeDao().searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return mainApi.searchRecipe(
                        query,
                        pageNumber);
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(final String recipeId) {
        return new NetworkBoundResource<Recipe, RecipeResponses>(appExecutors) {

            @Override
            public void saveCallResult(@NonNull RecipeResponses item) {
                // Recipe will be NULL if API key is expired
                if (item.getRecipe() != null) {
                    item.getRecipe().setTimestamp((int) (System.currentTimeMillis() / 1000)); // save time in seconds
                    recipeDao.getDatabase().recipeDao().insertRecipe(item.getRecipe());
                }
            }

            @Override
            public boolean shouldFetch(@Nullable Recipe data) {
                Log.d(TAG, "shouldFetch: recipe: " + Objects.requireNonNull(data).toString());
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                Log.d(TAG, "shouldFetch: current time: " + currentTime);
                int lastRefresh = data.getTimestamp();
                Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
                Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24)
                        + " days since this recipe was refreshed. 30 days must elapse.");
                if (((System.currentTimeMillis() / 1000) - data.getTimestamp()) >= Constants.RECIPE_REFRESH_TIME) {
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE? " + true);
                    return true;
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE? " + false);
                return false;
            }

            @NonNull
            @Override
            public LiveData<Recipe> loadFromDb() {
                return recipeDao.getDatabase().recipeDao().getRecipe(recipeId);
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeResponses>> createCall() {
                return mainApi.getRecipe(
                        recipeId
                );
            }

        }.getAsLiveData();
    }
}
