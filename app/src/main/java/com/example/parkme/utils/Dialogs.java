package com.example.parkme.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.parkme.R;

public class Dialogs {

    public static AlertDialog.Builder newMessageDialog(Context context,
                                                       String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.ok), null);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }

}
