package com.androidcollider.koljadnik.feedback;

import android.content.Context;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.storage.SongsDataSource;

import java.util.List;

public class FeedbackActivityModel implements FeedbackActivityMVP.Model {

    private final SongsDataSource songsRepository;
    private Context context;

    public FeedbackActivityModel(Context context, SongsDataSource songsRepository) {
        this.context = context;
        this.songsRepository = songsRepository;
    }

    @Override
    public String[] getActionsList() {
        return context.getResources().getStringArray(R.array.submit_array);
    }

    @Override
    public String getShowSendFeedbackActivityTitle() {
        return context.getResources().getString(R.string.letter_sending);
    }

    @Override
    public UiAction getCategoryList(OnReadListener<List<SongType>> listener) {
        return songsRepository.getSongTypes(listener);
    }
}
