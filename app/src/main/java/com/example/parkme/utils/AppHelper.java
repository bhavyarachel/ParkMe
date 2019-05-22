package com.example.parkme.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.parkme.R;

public class AppHelper {

    /**
     * Add Fragment to main container view.
     * Fragment won't be added to back stack.
     *
     * @param context
     * @param fragment
     */
    public static void addFragment(Context context, Fragment fragment) {
        addFragment(context, fragment, false);
    }

    /**
     * Add Fragment to main container view.
     *
     * @param context
     * @param fragment
     * @param addToBackStack
     */
    public static void addFragment(Context context, Fragment fragment,
                                   boolean addToBackStack) {
        getFragmentTransaction(context, fragment, "", fragment.getArguments(), addToBackStack).commit();
    }

    public static void replaceFragmentWithTag(FragmentActivity activity, Fragment fragment, String tag) {
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.addToBackStack(tag);
        mFragmentTransaction.replace(R.id.container, fragment);
        mFragmentTransaction.commit();
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment) {
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.container, fragment);
        mFragmentTransaction.commit();
    }


    /**
     * Return FragmentTransaction object for common container
     *
     * @param fragment
     * @param context
     * @param tag
     * @param bundle
     * @param addToBackStack
     * @return
     */
    public static FragmentTransaction getFragmentTransaction(Context context, Fragment fragment,
                                                             String tag, Bundle bundle, boolean addToBackStack) {
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        fragment.setArguments(bundle);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        return ft;
    }
}
