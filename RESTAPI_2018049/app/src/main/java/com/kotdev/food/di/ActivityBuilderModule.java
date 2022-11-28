package com.kotdev.food.di;


import com.kotdev.food.di.auth.AuthModule;
import com.kotdev.food.di.auth.AuthScope;
import com.kotdev.food.di.auth.AuthViewModelsModule;
import com.kotdev.food.di.main.MainFragmentBuildersModule;
import com.kotdev.food.di.main.MainModule;
import com.kotdev.food.di.main.MainScope;
import com.kotdev.food.di.main.MainViewModelsModule;
import com.kotdev.food.app.ui.activities.AuthActivity;
import com.kotdev.food.app.ui.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilderModule {


    @AuthScope
    @ContributesAndroidInjector (
            modules = {
                    AuthViewModelsModule.class,
                    AuthModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector (
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();
}
