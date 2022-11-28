package com.kotdev.food.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kotdev.food.R;
import com.kotdev.food.databinding.FragmentRecipesListBinding;
import com.kotdev.food.app.ui.adapters.RecipesRecyclerAdapter;
import com.kotdev.food.app.ui.activities.MainActivity;
import com.kotdev.food.helpers.util.VerticalSpaceItemDecoration;
import com.kotdev.food.app.viewmodels.ViewModelProviderFactory;
import com.kotdev.food.app.viewmodels.recipes.RecipesViewModel;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.kotdev.food.helpers.util.Constants.QUERY_EXHAUSTED;

public class RecipesListFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "RecipesListFragment";
    private FragmentRecipesListBinding binding;
    private RecipesViewModel viewModel;
    private static String key;
    private static boolean arguments = true;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RecipesRecyclerAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(RecipesViewModel.class);
        Log.d(TAG, "onViewCreated: " + arguments);
        ((MainActivity) requireActivity()).searchView.setVisibility(View.VISIBLE);
        if (arguments) {
            if (getArguments() != null) {
                adapter.displayLoading();
                key = requireArguments().getString("recipe");
                searchRecipeApi(key);
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        initSearchView();
        initRecyclerView();
        subscribeObservers();

    }

    private void searchRecipeApi(String query) {
        binding.recipeList.smoothScrollToPosition(0);
        viewModel.searchRecipesApi(query, 1);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void initSearchView() {
        SearchView searchView = ((MainActivity) requireActivity()).searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    adapter.displayLoading();
                    key = query;
                }
                searchRecipeApi(key);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arguments = true;
        ((MainActivity) requireActivity()).searchView.setIconified(true);
        ((MainActivity) requireActivity()).searchView.onActionViewCollapsed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arguments = false;
        viewModel.cancelSearchRequest();
    }

    private void subscribeObservers() {
        viewModel.getRecipes().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {

                switch (listResource.status) {
                    case LOADING: {
                        if (viewModel.getPageNumber() > 1) {
                            adapter.displayLoading();
                        } else {
                            adapter.displayOnlyLoading();
                        }
                        break;
                    }
                    case SUCCESS: {
                        adapter.hideLoading();
                        adapter.setPosts(listResource.data);
                        break;
                    }
                    case ERROR: {
                        Log.d(TAG, "onChanged: ERROR" + listResource.message);
                        Log.e(TAG, "onChanged: cannot refresh cache.");
                        Log.e(TAG, "onChanged: status: ERROR, #Recipes: " + Objects.requireNonNull(listResource.data).size());
                        adapter.hideLoading();
                        adapter.setPosts(listResource.data);
                        if (Objects.equals(listResource.message, QUERY_EXHAUSTED)) {
                            adapter.setQueryExhausted();
                        }
                        break;
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        binding.recipeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(15);
        binding.recipeList.addItemDecoration(itemDecoration);
        binding.recipeList.setAdapter(adapter);
        binding.recipeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!binding.recipeList.canScrollVertically(1)) {
                    viewModel.searchNextPage();
                }
            }
        });
        adapter.setClickCallback((position, model) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("id", model);
            Navigation.findNavController(requireView()).navigate(R.id.detalRecipeFragment, bundle);
        });
    }

    @Override
    public void onRefresh() {
        searchRecipeApi(key);
    }
}