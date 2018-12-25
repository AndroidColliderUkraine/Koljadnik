package com.androidcollider.koljadnik.feedback;

import android.content.Context;
import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.R;

import static com.androidcollider.koljadnik.constants.Settings.FEEDBACK_MAILS;

public class FeedbackActivityPresenter implements FeedbackActivityMVP.Presenter {

    @Nullable
    private FeedbackActivityMVP.View view;
    private FeedbackActivityMVP.Model model;

    private Context context;


    public FeedbackActivityPresenter(Context context, FeedbackActivityMVP.Model model) {
        this.model = model;
        this.context = context;
    }

    @Override
    public void setView(FeedbackActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        if (view != null) {
            view.updateActionAdapter(model.getActionsList());
        }
    }

    @Override
    public void clickSendFeedbackBtn() {
        if (view != null) {
            String text = view.getText();

            if (text.isEmpty()) {
                view.showErrorToast(context.getString(R.string.you_cannot_send));
            } else {
                view.showSendFeedbackActivity(FEEDBACK_MAILS, model.getShowSendFeedbackActivityTitle(),
                    view.getSelectedActionText(), text);
            }
        }
    }
}
