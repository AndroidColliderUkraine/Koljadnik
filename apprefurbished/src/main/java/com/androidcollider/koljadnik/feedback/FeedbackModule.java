package com.androidcollider.koljadnik.feedback;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FeedbackModule {

    @Provides
    public FeedbackActivityMVP.Presenter provideFeedbackActivityPresenter(FeedbackActivityMVP.Model model, Context context) {
        return new FeedbackActivityPresenter(context, model);
    }

    @Provides
    public FeedbackActivityMVP.Model provideFeedbackActivityModel(Context context) {
        return new FeedbackActivityModel(context);
    }

}
