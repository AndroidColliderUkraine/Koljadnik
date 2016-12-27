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


public abstract class CommonToolbarActivity extends CommonActivity {

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
        }
    }

    protected void setToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuRes =  getMenuRes();

        if (menuRes > 0) {
            getMenuInflater().inflate(menuRes, menu);
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getMenuRes();

    protected abstract boolean isDisplayHomeAsUpEnabled();
}
