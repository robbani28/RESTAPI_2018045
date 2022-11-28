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
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.kotdev.food.R;
import com.kotdev.food.databinding.FragmentProfilerBinding;
import com.kotdev.food.app.ui.adapters.ProfilePostRecyclerAdapter;
import com.kotdev.food.app.ui.activities.MainActivity;
import com.kotdev.food.app.ui.adapters.HeaderAdapter;
import com.kotdev.food.helpers.util.VerticalSpaceItemDecoration;
import com.kotdev.food.app.viewmodels.ViewModelProviderFactory;
import com.kotdev.food.app.viewmodels.profile.ProfileViewModel;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment {

    public static final String TAG = "ProfileFragment";

    private ProfileViewModel viewModel;
    private FragmentProfilerBinding binding;

    @Inject
    ConcatAdapter concatAdapter;

    @Inject
    ProfilePostRecyclerAdapter adapter;

    @Inject
    HeaderAdapter headerAdapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).searchView.setVisibility(View.GONE);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ProfileViewModel.class);
        initRecyclerView();
        subscribeObservers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    private void subscribeObservers() {
        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case AUTHENTICATED: {
                        headerAdapter.setUsernames(Objects.requireNonNull(userAuthResource.data).getUsername());
                        headerAdapter.setEmails(userAuthResource.data.getEmail());
                        headerAdapter.setWebsites(userAuthResource.data.getWebsite());
                        headerAdapter.setIcons(R.drawable.logo);
                        Toast.makeText(getContext(), "" + userAuthResource.data.getUsername(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case ERROR: {
                        headerAdapter.setWebsites("error");
                        headerAdapter.setUsernames("error");
                        headerAdapter.setEmails(userAuthResource.message);
                        break;
                    }
                }
            }
        });
        viewModel.observePosts().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        Log.d(TAG, "onChanged: LOADING");
                        break;
                    }
                    case SUCCESS: {
                        Log.d(TAG, "onChanged: get posts");
                        adapter.setPosts(listResource.data);
                        headerAdapter.setSize(adapter.getItemCount());
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

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(15);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(concatAdapter);
    }
}
