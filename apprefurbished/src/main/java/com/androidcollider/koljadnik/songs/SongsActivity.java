package com.androidcollider.koljadnik.songs;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.song_types.SongTypeAdapter;
import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import butterknife.BindView;


public class SongsActivity extends CommonToolbarActivity implements SongsActivityMVP.View {

    public final static String EXTRA_SONG_TYPE_ID = "song_type_id";

    @BindView(R.id.rv_songs)
    RecyclerView rvSongs;

    @Inject
    SongsActivityMVP.Presenter presenter;

    private SongsAdapter songsAdapter;
    private int typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);

        if (getIntent().hasExtra(EXTRA_SONG_TYPE_ID)){
            typeId = getIntent().getIntExtra(EXTRA_SONG_TYPE_ID, 0);
        }

        songsAdapter = new SongsAdapter(itemClickListener);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
        rvSongs.setAdapter(songsAdapter);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_song_list;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.main_menu;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData(typeId);
    }

    @Override
    public void updateAdapter(List<SongItemViewModel> songItemViewModels) {
        songsAdapter.updateData(songItemViewModels);
    }

    @Override
    public void showSongDetailsUI(int songId) {

    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener itemClickListener = view -> {
        presenter.openSongDetailsUI(view.getTag());
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_add) {

        }
        return true;
    }
}
