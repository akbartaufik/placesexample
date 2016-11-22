package com.opika.placeautocomplete.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.opika.placeautocomplete.R;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    protected ProgressDialog showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.prog_dialog_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    protected void dismissDialog() {
        progressDialog.dismiss();
    }
}
