package com.kotdev.food.app.ui.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.RequestManager;
import com.kotdev.food.R;
import com.kotdev.food.databinding.ActivityAuthBinding;
import com.kotdev.food.app.viewmodels.ViewModelProviderFactory;
import com.kotdev.food.app.viewmodels.auth.AuthViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity {

    public static final String TAG = " AuthActivity ";

    private AuthViewModel authViewModel;

    private ActivityAuthBinding binding;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    Drawable logo;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel.class);

        setLogo();
        subscribeObservers();
        binding.loginButton.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(Objects.requireNonNull(binding.userIdInput.getText()).toString())) {
                authViewModel.authWithId(binding.userIdInput.getText().toString());
            }
        });
    }

    private void setLogo() {
        requestManager.load(logo).into((ImageView) findViewById(R.id.login_logo));
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void subscribeObservers() {
        authViewModel.observeAuthState().observe(this, authResource -> {
            if (authResource != null) {
                switch (authResource.status) {
                    case LOADING: {
                        binding.loginButton.startAnimation();
                        break;
                    }
                    case AUTHENTICATED: {
                        binding.loginButton.doneLoadingAnimation(R.color.color_button_default, BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_done_white_48dp));
                        Log.d(TAG, "onChanged: LOGIN SUCCESS: " + Objects.requireNonNull(authResource.data).getEmail());
                        onLoginSuccess();
                        break;
                    }
                    case ERROR: {
                        binding.loginButton.revertAnimation();
                        Toast.makeText(AuthActivity.this, authResource.message +
                                "\nincorrect number", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NOT_AUTHENTICATED: {
                        binding.loginButton.revertAnimation();
                        break;
                    }
                }
            }
        });
    }
}