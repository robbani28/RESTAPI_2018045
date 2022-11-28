package com.kotdev.food.app.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponses {

    @SerializedName("recipe")
    @Expose()
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @SerializedName("error")
    @Expose()
    private String error;

    @Nullable
    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RecipeResponses{" +
                "recipe=" + recipe +
                ", error='" + error + '\'' +
                '}';
    }
}
