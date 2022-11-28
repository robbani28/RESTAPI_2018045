package com.kotdev.food.app.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.kotdev.food.R;
import com.kotdev.food.databinding.FragmentPostBinding;
import com.kotdev.food.app.ui.adapters.CategoriesRecipeRecyclerAdapter;
import com.kotdev.food.app.ui.activities.MainActivity;
import com.kotdev.food.helpers.util.VerticalSpaceItemDecoration;
import com.kotdev.food.app.viewmodels.ViewModelProviderFactory;
import com.kotdev.food.app.viewmodels.posts.PostsViewModel;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PostsFragment extends DaggerFragment {

    private static final String TAG = "PostsFragment";

    private PostsViewModel postsViewModel;
    private FragmentPostBinding binding;

    @Inject
    CategoriesRecipeRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        postsViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(PostsViewModel.class);
        initRecyclerView();
        subscribeObservers();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        ((MainActivity) requireActivity()).searchView.setVisibility(View.VISIBLE);
        ((MainActivity) requireActivity()).searchView.setOnSearchClickListener(v -> {
            if (!Objects.requireNonNull(Objects.requireNonNull(navController.getCurrentDestination()).getLabel()).toString().equals("Recipes")) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_global_recipesListFragment);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(15);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(adapter);
        adapter.setClickCallback((position, model) -> {
            Bundle bundle = new Bundle();
            bundle.putString("recipe", model.getTitle());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_global_recipesListFragment, bundle);
            ((MainActivity) requireActivity()).searchView.clearFocus();
        });
    }


    private void subscribeObservers() {
        postsViewModel.observePosts().removeObservers(getViewLifecycleOwner());
        postsViewModel.observePosts().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onChanged: LOADING");
                        break;
                    }
                    case SUCCESS: {
                        Log.d(TAG, "onChanged: get posts");
                        adapter.setPosts(listResource.data);
                        break;
                    }
                    case ERROR: {
                        Log.d(TAG, "onChanged: ERROR" + listResource.message);
                        break;
                    }
                }
            }
        });
    }
}
