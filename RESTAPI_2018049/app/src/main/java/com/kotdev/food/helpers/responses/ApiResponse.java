package com.kotdev.food.helpers.responses;

import com.kotdev.food.app.models.RecipeResponses;
import com.kotdev.food.app.models.RecipeSearchResponse;
import com.kotdev.food.helpers.CheckRecipeApiKey;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponse<T> create(Throwable error){
        return new ApiErrorResponse<>(error.getMessage().equals("") ? error.getMessage() : "Unknown error\nCheck network connection");
    }

    public ApiResponse<T> create(Response<T> response){

        if(response.isSuccessful()){
            T body = response.body();

            // make sure api key is valid and not expired
            if(body instanceof RecipeSearchResponse){
                if(!CheckRecipeApiKey.isRecipeApiKeyValid((RecipeSearchResponse)body)){
                    String errorMsg = "Api key invalid or expired.";
                    return new ApiErrorResponse<>(errorMsg);
                }
            }
            else if(body instanceof RecipeResponses){
                if(!CheckRecipeApiKey.isRecipeApiKeyValid((RecipeResponses)body)){
                    String errorMsg = "Api key invalid or expired.";
                    return new ApiErrorResponse<>(errorMsg);
                }
            }
            if(body == null || response.code() == 204){ // 204 is empty response
                return new ApiEmptyResponse<>();
            }
            else{
                return new ApiSuccessResponse<>(body);
            }
        }
        else{
            String errorMsg = "";
            try {
                errorMsg = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
                errorMsg = response.message();
            }
            return new ApiErrorResponse<>(errorMsg);
        }
    }

    /**
     * Generic success response from api
     * @param <T>
     */
    public class ApiSuccessResponse<T> extends ApiResponse<T> {

        private T body;

        ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }

    }

    /**
     * Generic Error response from API
     * @param <T>
     */
    public class ApiErrorResponse<T> extends ApiResponse<T> {

        private String errorMessage;

        ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }


    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    public class ApiEmptyResponse<T> extends ApiResponse<T> { }

}

