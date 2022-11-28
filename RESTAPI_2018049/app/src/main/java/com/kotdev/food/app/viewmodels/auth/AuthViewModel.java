package com.kotdev.food.app.viewmodels.auth;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.kotdev.food.SessionManager;
import com.kotdev.food.app.models.User;
import com.kotdev.food.network.auth.AuthApi;
import com.kotdev.food.helpers.resource.AuthResource;


import javax.inject.Inject;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    public static final String TAG = "AuthViewModel";

    private final AuthApi authApi;
    private final SessionManager sessionManager;

    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.authApi = authApi;
        this.sessionManager = sessionManager;
    }

    public void authWithId(String userId) {
        Log.d(TAG, "authWithId: attempting to login.");
        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(String userId) {
        return LiveDataReactiveStreams.fromPublisher(
                authApi.getUser(userId)
                        .onErrorReturn(throwable -> {
                            User errorUser = new User();
                            errorUser.setId(-1);
                            return errorUser;
                        })
                        .map(user -> {
                            if (user.getId() == -1) {
                                return AuthResource.error("Could not authenticates", (User) null);
                            }
                            return AuthResource.authenticated(user);
                        })
                        .subscribeOn(Schedulers.io())
        );
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }
}
