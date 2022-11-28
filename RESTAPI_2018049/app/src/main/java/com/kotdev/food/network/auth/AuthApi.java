package com.kotdev.food.network.auth;

import com.kotdev.food.app.models.Post;
import com.kotdev.food.app.models.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("users/{id}")
    Flowable<User> getUser(@Path("id") String id);

    @GET("posts")
    Flowable<List<Post>> getPosts(@Query("userId") int id);
}
