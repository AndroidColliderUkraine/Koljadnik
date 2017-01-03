package com.androidcollider.koljadnik.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.utils.DialogManager;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class CommonActivity extends AppCompatActivity implements CoomonView {

    @Inject
    DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewRes());
        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
    }

    protected abstract int getContentViewRes();

    @Override
    public void blockUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!isDestroyed()) {
                dialogManager.showProgressProcessingDialog(this);
            }
        } else {
            dialogManager.showProgressProcessingDialog(this);
        }
    }

    @Override
    public void unblockUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!isDestroyed()) {
                dialogManager.dismissProgressDialog();
            }
        } else {
            dialogManager.dismissProgressDialog();
        }
    }

    @Override
    protected void onDestroy() {
        unblockUi();
        super.onDestroy();
    }
}
