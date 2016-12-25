package com.androidcollider.koljadnik.songs_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.feedback.FeedbackActivity;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.song_details.SongDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SongsActivity extends CommonToolbarActivity implements SongsActivityMVP.View {

    public final static String EXTRA_SONG_TYPE_ID = "song_type_id";

    @BindView(R.id.rv_songs)
    RecyclerView rvSongs;

    @BindView(R.id.cnt_order_menu)
    View cnOrderMenu;

    @BindView(R.id.et_search_song)
    EditText etSearchSong;

    @Inject
    SongsActivityMVP.Presenter presenter;

    private SongsAdapter songsAdapter;
    private int typeId;
    private boolean isOrderMenuOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_SONG_TYPE_ID)){
            typeId = getIntent().getIntExtra(EXTRA_SONG_TYPE_ID, 0);
        }
        buildAndInjectComponent();

        songsAdapter = new SongsAdapter(itemClickListener);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
        rvSongs.setAdapter(songsAdapter);

        etSearchSong.addTextChangedListener(searchListener);
    }

    private void buildAndInjectComponent() {
        DaggerSongsComponent.builder()
                .appComponent(((App)getApplication()).getAppComponent())
                .songsModule(new SongsModule(typeId))
                .build()
                .inject(this);
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
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData();
    }

    @Override
    public void updateAdapter(List<SongItemViewModel> songItemViewModels) {
        songsAdapter.updateData(songItemViewModels);
    }

    @Override
    public void showSongDetailsUI(int songId) {
        Intent intent = new Intent(this, SongDetailsActivity.class);
        intent.putExtra(SongDetailsActivity.EXTRA_SONG_ID, songId);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener itemClickListener = view -> {
        presenter.openSongDetailsUI(view.getTag());
    };

    private TextWatcher searchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            presenter.proceedSearch(editable.toString());
        }
    };

    @OnClick({R.id.btn_sort, R.id.btn_sort_by_alphabet, R.id.btn_sort_by_rating,})
    public void onClickSort(View v){
        switch (v.getId()){
            case R.id.btn_sort:
                presenter.clickOnOrderMenuBtn();
                break;
            case R.id.btn_sort_by_alphabet:
                presenter.clickOnOrderBtn(OrderType.BY_ALPHABET);
                break;
            case R.id.btn_sort_by_rating:
                presenter.clickOnOrderBtn(OrderType.BY_RATING);
                break;
        }
    }

    @Override
    public void switchOrderMenuVisibility(){
        isOrderMenuOpened = !isOrderMenuOpened;
        cnOrderMenu.setVisibility(isOrderMenuOpened ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.btn_add) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        return true;
    }
}