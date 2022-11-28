package com.kotdev.food.network.main;

import androidx.lifecycle.LiveData;

import com.kotdev.food.app.models.RecipeResponses;
import com.kotdev.food.app.models.RecipeSearchResponse;
import com.kotdev.food.helpers.responses.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainApi {

   @GET("api/search")
   LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
          /* @Query("key") String key,*/
           @Query("q") String query,
           @Query("page") int page
   );

    @GET("api/get")
    LiveData<ApiResponse<RecipeResponses>> getRecipe(
            /* @Query("key") String key,*/
            @Query("rId") String recipe_id
    );
}