package com.androidcollider.koljadnik.song_types;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidcollider.koljadnik.R;

import butterknife.BindView;


public class SongTypesActivity extends Activity implements SongTypesActivityMVP.View{

    @BindView(R.id.rv_types)
    RecyclerView rvTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_types);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    public void setListLayoutManager(RecyclerView.LayoutManager listLayoutManager) {
        rvTypes.setLayoutManager(listLayoutManager);
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
