package com.kotdev.food.app.viewmodels.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;


import com.kotdev.food.SessionManager;
import com.kotdev.food.app.models.Post;
import com.kotdev.food.app.models.User;
import com.kotdev.food.network.auth.AuthApi;
import com.kotdev.food.helpers.resource.AuthResource;
import com.kotdev.food.helpers.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

    public static final String TAG = "ProfileViewModel";

    private final SessionManager sessionManager;
    private final AuthApi authApi;
    private MediatorLiveData<Resource<List<Post>>> posts;

    @Inject
    public ProfileViewModel(SessionManager sessionManager, AuthApi authApi) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<List<Post>>> observePosts(){
        if(posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));

            Log.d(TAG, "observePosts: user id: "
                    + Objects.requireNonNull(Objects.requireNonNull(sessionManager.getAuthUser().getValue()).data).getId());
            final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(

                    authApi.getPosts(Objects.requireNonNull(sessionManager.getAuthUser().getValue().data).getId())

                            .onErrorReturn(throwable -> {
                                Log.e(TAG, "apply: ", throwable);
                                Post post = new Post();
                                post.setId(-1);
                                ArrayList<Post> posts = new ArrayList<>();
                                posts.add(post);
                                return posts;
                            })

                            .map((Function<List<Post>, Resource<List<Post>>>) posts -> {
                                if (posts.size() > 0) {
                                    if (posts.get(0).getId() == -1) {
                                        return Resource.error("Something went wrong", null);
                                    }
                                }
                                return Resource.success(posts);
                            })
                            .subscribeOn(Schedulers.io()));

            posts.addSource(source, listResource -> {
                posts.setValue(listResource);
                posts.removeSource(source);
            });
        }
        return posts;
    }
}
