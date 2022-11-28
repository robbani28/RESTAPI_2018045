package com.kotdev.food.di;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.kotdev.food.R;
import com.kotdev.food.helpers.util.Constants;
import com.kotdev.food.helpers.livedatafactory.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kotdev.food.helpers.util.Constants.CONNECTION_TIMEOUT;
import static com.kotdev.food.helpers.util.Constants.READ_TIMEOUT;
import static com.kotdev.food.helpers.util.Constants.WRITE_TIMEOUT;

@Module
public class AppModule {


    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                // establish connection with server
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                // time between each byte read from server
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                // time between each byte sent to server
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    @Singleton
    @Provides
    @Named("Auth")
    static Retrofit provideRetrofitInstance(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Singleton
    @Provides
    @Named("Recipes")
    static Retrofit provideRetrofitFood() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_IMAGE)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions.placeholderOf(R.drawable.logo)
                .error(R.drawable.ic_launcher_background);
    }

    @Singleton
    @Provides
    static RequestManager provideRequestManager(Application application, RequestOptions requestOptions) {
        return Glide.with(application).setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static Drawable provideDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }

    @Singleton
    @Provides
    static Context provideContext(Application application){
        return application.getApplicationContext();
    }
}
