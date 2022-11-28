package com.kotdev.food;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.kotdev.food.app.models.Recipe;
import com.kotdev.food.app.models.User;
import com.kotdev.food.helpers.resource.AuthResource;
import com.kotdev.food.helpers.resource.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";

    private final MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<Recipe>>> recipeLiveData = new MediatorLiveData<>();


    @Inject
    public SessionManager() {
    }

    public void authenticateWithId(final LiveData<AuthResource<User>> source) {
        cachedUser.setValue(AuthResource.loading(null));
        cachedUser.addSource(source, userAuthResource -> {
            cachedUser.setValue(userAuthResource);
            cachedUser.removeSource(source);
        });
    }

    public void recipes(final LiveData<Resource<List<Recipe>>> source) {
        recipeLiveData.setValue(Resource.loading(null));
        recipeLiveData.addSource(source, userAuthResource -> {
            recipeLiveData.setValue(userAuthResource);
            recipeLiveData.removeSource(source);
        });
    }

    public void logOut() {
        Log.d(TAG, "logOut: logging out...");
        cachedUser.setValue(AuthResource.logout());
    }


    public LiveData<AuthResource<User>> getAuthUser(){
        return cachedUser;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        return recipeLiveData;
    }

}

