package com.kotdev.food.di.auth;


import com.kotdev.food.network.auth.AuthApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    @AuthScope
    @Provides
    static AuthApi provideAuthApi(@Named("Auth") Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
}
