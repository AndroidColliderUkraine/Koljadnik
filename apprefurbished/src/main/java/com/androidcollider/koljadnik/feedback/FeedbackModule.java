package com.androidcollider.koljadnik.feedback;

import android.content.Context;

import com.androidcollider.koljadnik.storage.SongsDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class FeedbackModule {

    @Provides
    public FeedbackActivityMVP.Presenter provideFeedbackActivityPresenter(FeedbackActivityMVP.Model model, Context context) {
        return new FeedbackActivityPresenter(context, model);
    }

    @Provides
    public FeedbackActivityMVP.Model provideFeedbackActivityModel(Context context, SongsDataSource songsDataSource) {
        return new FeedbackActivityModel(context, songsDataSource);
    }

}
