package com.example.parkme.views.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.R;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfUserIsLoggedIn();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkIfUserIsLoggedIn() {
        User user = UserTableHelper.getCurrentUser();
        if(user!=null && user.isLoggedIn()){
            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}

