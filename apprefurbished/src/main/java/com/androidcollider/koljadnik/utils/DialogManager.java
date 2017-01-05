package com.androidcollider.koljadnik.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidcollider.koljadnik.R;

import javax.inject.Inject;

/**
 * Description of DialogManager
 *
 * @author Alexey Verbitskiy <averbitskiy@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package net.dressbox.app.utils
 */

public final class DialogManager {

    private MaterialDialog progressDialog;
    private MaterialDialog.Builder mDialogBuilder;
    private Context mContext;

    @Inject
    DialogManager(Context context) {
        mContext = context;
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public void showProgressProcessingDialog(Context context) {
        showProgressDialog(mContext.getString(R.string.wait_please), context);
    }

    public void showProgressDialog(String message, Context context) {
        if (progressDialog == null) {
            mDialogBuilder = new MaterialDialog.Builder(context)
                    .content(message)
                    .progress(true, 0)
                    .contentGravity(GravityEnum.CENTER)
                    .cancelable(false)
                    .backgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
            progressDialog = mDialogBuilder.show();
            Log.i("DialogManager", "UI BLOCKED");
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
            Log.i("DialogManager", "UI UNBLOCKED");
        } else {
            Log.i("DialogManager", "Dialog is already dismissed");
        }
    }
}
