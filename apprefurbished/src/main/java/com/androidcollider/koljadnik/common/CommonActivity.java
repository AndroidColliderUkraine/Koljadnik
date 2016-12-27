package com.androidcollider.koljadnik.common;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.utils.DialogManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class CommonActivity extends AppCompatActivity implements CoomonView{

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
        dialogManager.showProgressProcessingDialog(this);
    }

    @Override
    public void unblockUi() {
        dialogManager.dismissProgressDialog();
    }
}
