package com.example.parkme.views.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.parkme.R;
import com.example.parkme.api.ApiInterface;
import com.example.parkme.api.response.BaseResponse;
import com.example.parkme.constants.ApiConstants;
import com.example.parkme.constants.AppConstants;
import com.example.parkme.dbflow.TableHelpers.UserTableHelper;
import com.example.parkme.dbflow.models.User;
import com.example.parkme.utils.AppHelper;
import com.example.parkme.utils.Helper;
import com.example.parkme.utils.RetrofitHelper;
import com.example.parkme.views.fragments.MyBookingsFragment;
import com.example.parkme.views.fragments.PickDestinationFragment;
import com.example.parkme.views.fragments.UserProfileFragment;
import com.google.android.material.navigation.NavigationView;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView mTextViewTitle;

    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        hideBackButton();

        mProgressDialog = new ProgressDialog(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(navigationView);

        User user = UserTableHelper.getCurrentUser();
        if(user!=null) {
            View header = navigationView.getHeaderView(0);
            TextView textView;
            if (user.getUserName() != null) {
                textView = (TextView) header.findViewById(R.id.tv_user_name);
                textView.setText(user.getUserName());
            }
            if (user.getEmail() != null) {
                textView = (TextView) header.findViewById(R.id.tv_user_email);
                textView.setText(user.getEmail());
            }
        }

        AppHelper.addFragment(this, PickDestinationFragment.newInstance());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerToggle.onOptionsItemSelected(item)) {
                    mDrawer.openDrawer(GravityCompat.START);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    /**
     * Set title of toolbar
     *
     * @param text
     */
    public void setTitleText(String text) {
        mTextViewTitle.setText(text);
    }

    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(AppConstants.EMPTY_STRING);
    }

    public void hideBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(AppConstants.EMPTY_STRING);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_pick_destination:
                AppHelper.replaceFragment(this, PickDestinationFragment.newInstance());
                break;
            case R.id.nav_user_profile:
                AppHelper.replaceFragment(this, UserProfileFragment.newInstance());
                break;
            case R.id.nav_my_bookings:
                AppHelper.replaceFragment(this, MyBookingsFragment.newInstance());
                break;
            case R.id.nav_logout:
                callLogoutAPI();
                break;
            default:
                AppHelper.replaceFragment(this, PickDestinationFragment.newInstance());
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    /**
     * API call to logout
     */
    private void callLogoutAPI() {
        mProgressDialog.setMessage(getString(R.string.logging_out));
        Helper.showProgress(mProgressDialog);
        User user = UserTableHelper.getCurrentUser();
        if(Helper.isDeviceConnected(this)){
            Helper.showProgress(mProgressDialog);
            ApiInterface apiInterface = new RetrofitHelper().getService(ApiInterface.class);
            Call<BaseResponse> call = apiInterface.logout(user.getSessionKey());
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Helper.dismissProgress(mProgressDialog);
                    if (response.body() != null) {
                        if (response.body().getStatus() == ApiConstants.STATUS_SUCCESS) {
                            UserTableHelper.deleteCurrentUserInfo();
                            finish();
                            loadLoginScreen();
                        } else {
                            Helper.showSnackBar(response.body().getMessage(), mDrawer);
                        }
                    } else {
                        Helper.showSnackBar(getString(R.string.something_went_wrong), mDrawer);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Helper.dismissProgress(mProgressDialog);
                    Helper.showSnackBar(getString(R.string.something_went_wrong),mDrawer);
                }
            });
        }else{
            Helper.dismissProgress(mProgressDialog);
            Helper.showSnackBar(getString(R.string.network_error),mDrawer);
        }
    }

    /**
     * Call Login screen intent
     */
    private void loadLoginScreen() {
        Intent activityIntent = new Intent();
        activityIntent.setClass(this, LoginActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityIntent);
    }


}
