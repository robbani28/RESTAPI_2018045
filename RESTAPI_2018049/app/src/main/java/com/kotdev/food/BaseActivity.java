package com.kotdev.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kotdev.food.app.ui.activities.AuthActivity;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    public static final String TAG = "BaseActivity";

    @Inject
    public SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager.getAuthUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case LOADING: {

                        break;
                    }
                    case AUTHENTICATED: {
                        Log.d(TAG, "onChanged: LOGIN SUCCESS: " + Objects.requireNonNull(userAuthResource.data).getEmail());
                        break;
                    }
                    case ERROR: {
                        Toast.makeText(BaseActivity.this, userAuthResource.message +
                                "\nincorrect number", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NOT_AUTHENTICATED: {
                        Intent intent = new Intent(BaseActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });
    }
}
