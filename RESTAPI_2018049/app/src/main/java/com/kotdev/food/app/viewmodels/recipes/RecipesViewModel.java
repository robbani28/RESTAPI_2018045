package com.kotdev.food.app.viewmodels.recipes;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import com.kotdev.food.di.main.MainScope;
import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.repositories.RecipeRepository;
import com.kotdev.food.helpers.resource.Resource;
import java.util.List;
import javax.inject.Inject;

import static com.kotdev.food.helpers.util.Constants.QUERY_EXHAUSTED;

@MainScope
public class RecipesViewModel extends ViewModel {

    private static final String TAG = "RecipeListViewModel";

    private final RecipeRepository mRecipeRepository;
    private final MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private boolean isQueryExhausted;
    private String query;
    private int pageNumber;
    private boolean isPerformingQuery;
    private boolean cancelRequest;

    @Inject
    public RecipesViewModel(RecipeRepository mRecipeRepository) {
        this.mRecipeRepository = mRecipeRepository;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(!isPerformingQuery){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    public void searchNextPage(){
        if(!isQueryExhausted && !isPerformingQuery){
            pageNumber++;
            executeSearch();
        }
    }

    private void executeSearch(){
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<Recipe>>> repositorySource = mRecipeRepository.searchRecipesApi(query, pageNumber);
        recipes.addSource(repositorySource, listResource -> {
            if(!cancelRequest) {
                if (listResource != null) {
                    recipes.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                recipes.setValue(new Resource<>(
                                        Resource.Status.ERROR,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                                isPerformingQuery = true;
                            }
                        }
                        recipes.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        recipes.removeSource(repositorySource);
                    }
                } else {
                    recipes.removeSource(repositorySource);
                }
            } else{
                recipes.removeSource(repositorySource);
            }
        });
    }

    public void cancelSearchRequest(){
        if(isPerformingQuery){
            Log.d(TAG, "cancelSearchRequest: canceling the search request.");
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }
}

