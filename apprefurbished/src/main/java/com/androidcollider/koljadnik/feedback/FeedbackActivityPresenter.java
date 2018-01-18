package com.androidcollider.koljadnik.feedback;

import android.content.Context;
import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

import static com.androidcollider.koljadnik.contants.Settings.FEEDBACK_MAILS;

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
            model.getCategoryList(new OnReadListener<List<SongType>>() {
                @Override
                public void onSuccess(List<SongType> songTypes) {
                    if (songTypes != null && !songTypes.isEmpty()) {
                        if (view != null) {
                            String[] categories = new String[songTypes.size() + 1];
                            for (int i = 0; i < songTypes.size(); i++) {
                                categories[i] = songTypes.get(i).getName();
                            }
                            categories[categories.length-1] = context.getString(R.string.other);
                            view.updateCategoryAdapter(categories);
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (view != null) {
                        view.showErrorToast(error);
                    }
                }
            });
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
