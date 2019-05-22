package com.example.parkme.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.content.ContentValues.TAG;

public class Helper {

    /**
     * Show Toast message
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(18);
            toast.show();
        } catch (Exception err) {
            Log.d("ERROR Toast(show)", err.getMessage());
        }
    }

    /**
     * Checks for device's network connection
     *
     * @param context
     * @return
     */
    public static boolean isDeviceConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) isConnected = true;
        return isConnected;
    }

    /**
     * Show progress dialog
     *
     * @param progressDialog
     */
    public static void showProgress(ProgressDialog progressDialog) {
        try {
            dismissProgress(progressDialog);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "@@@@showProgress " + e.getLocalizedMessage());

        }
    }

    /**
     * Dismiss provided progress dialog
     *
     * @param progressDialog
     */
    public static void dismissProgress(ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) progressDialog.dismiss();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * Email validation
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Show SnackBar
     * @param message message to be displayed on snack bar
     * @param view root layout view
     */
    public static Snackbar showSnackBar(String message, View view){
        if (view == null) {
            return null;
        }
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(1000);
        snackbar.show();
        return snackbar;
    }
}
