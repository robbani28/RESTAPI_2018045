package com.kotdev.food.helpers;

import com.kotdev.food.app.models.RecipeResponses;
import com.kotdev.food.app.models.RecipeSearchResponse;

public class CheckRecipeApiKey {

    public static boolean isRecipeApiKeyValid(RecipeSearchResponse response) {
        return response.getError() == null;
    }

    public static boolean isRecipeApiKeyValid(RecipeResponses response) {
        return response.getError() == null;
    }
}
