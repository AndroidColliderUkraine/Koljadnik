package com.androidcollider.koljadnik.common;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidcollider.koljadnik.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewRes());
        ButterKnife.bind(this);
    }

    protected abstract int getContentViewRes();
}
