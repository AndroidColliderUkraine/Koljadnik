package com.androidcollider.koljadnik.song_details;

import android.content.Context;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.storage.SongsDataSource;

public class SongDetailsActivityModel implements SongDetailsActivityMVP.Model {

    private SongsDataSource songsDataSource;
    private int songId;
    private Context context;

    public SongDetailsActivityModel(Context context, SongsDataSource songsDataSource, int songId) {
        this.songsDataSource = songsDataSource;
        this.songId = songId;
        this.context = context;
    }

    @Override
    public UiAction getSong(OnReadListener<SongDetailsViewModel> listener) {
        return songsDataSource.getSongById(songId, new OnReadListener<Song>() {
            @Override
            public void onSuccess(Song song) {
                listener.onSuccess(new SongDetailsViewModel(song.getText(), song.getSource()));
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

    }

    @Override
    public UiAction getSongText(OnReadListener<String> listener) {
        return songsDataSource.getSongById(songId, new OnReadListener<Song>() {
            @Override
            public void onSuccess(Song song) {
                listener.onSuccess(song.getText() + context.getString(R.string.slava_ukraini));
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    @Override
    public UiAction getShareData(OnReadListener<ShareModel> listener) {
        return songsDataSource.getSongById(songId, new OnReadListener<Song>() {
            @Override
            public void onSuccess(Song song) {
                listener.onSuccess(new ShareModel(context.getString(R.string.song_sending),
                        song.getText() + context.getString(R.string.slava_ukraini),
                        context.getString(R.string.koljadka) + song.getName()));
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }
}
