package com.example.parkme.views.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.utils.AppHelper;
import com.example.parkme.views.fragments.LoginFragment;
import com.example.parkme.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.layout_custom_action_bar);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppHelper.addFragment(this, LoginFragment.newInstance());
    }
}
