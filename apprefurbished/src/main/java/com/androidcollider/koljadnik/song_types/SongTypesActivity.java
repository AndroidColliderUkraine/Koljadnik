package com.androidcollider.koljadnik.song_types;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.root.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SongTypesActivity extends Activity implements SongTypesActivityMVP.View{

    @BindView(R.id.rv_types) RecyclerView rvTypes;

    @Inject
    SongTypesActivityMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_types);
        ButterKnife.bind(this);

        ((App)getApplication()).getComponent().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 10) {
            if (getActionBar() != null) {
                getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_song_types, menu);
        return true;
    }

    @Override
    public void setAdapterToList(RecyclerView.Adapter adapter) {
        rvTypes.setAdapter(adapter);
    }

    @Override
    public void setLinearLayoutManager() {
        rvTypes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.add_song) {
            Intent intent = new Intent(this, SubmitActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }*/
        return true;
    }
}
