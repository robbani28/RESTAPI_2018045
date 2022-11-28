package com.kotdev.food.di.auth;

import androidx.lifecycle.ViewModel;


import com.kotdev.food.di.ViewModelKey;
import com.kotdev.food.app.viewmodels.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {


    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel authViewModel);
}
