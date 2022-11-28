package com.kotdev.food.app.viewmodels.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.kotdev.food.app.models.Category;
import com.kotdev.food.helpers.resource.Resource;
import com.kotdev.food.helpers.util.PostsGeneration;

import java.util.List;

import javax.inject.Inject;


public class PostsViewModel extends ViewModel {

    public static final String TAG = "PostsViewModel";

    private MediatorLiveData<Resource<List<Category>>> posts;

    @Inject
    public PostsViewModel() {
    }

    public LiveData<Resource<List<Category>>> observePosts(){
        if(posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));

            final LiveData<Resource<List<Category>>> source = PostsGeneration.getRecipeCategories();

            posts.addSource(source, listResource -> {
                posts.setValue(listResource);
                posts.removeSource(source);
            });
        }
        return posts;
    }
}
