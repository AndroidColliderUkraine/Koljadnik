package com.androidcollider.koljadnik.feedback;

import android.content.Context;

import com.androidcollider.koljadnik.R;

public class FeedbackActivityModel implements FeedbackActivityMVP.Model {

    private Context context;

    public FeedbackActivityModel(Context context) {
        this.context = context;
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
