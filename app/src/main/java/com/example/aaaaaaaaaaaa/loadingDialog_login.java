package com.example.aaaaaaaaaaaa;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.aaaaaaaaaaaa.R;

public class loadingDialog_login {
    private Activity activity;
    private AlertDialog dialog;
    public loadingDialog_login(Activity activity)
    {
        this.activity=activity;
    }
    void startLoadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();


    }
    void dismissDialog()
    {
        dialog.dismiss();
    }
}
