package com.androidcollider.koljadnik.feedback;

import android.content.Context;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.storage.SongsDataSource;

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
}
