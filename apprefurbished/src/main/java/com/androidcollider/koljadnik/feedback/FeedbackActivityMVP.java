package com.androidcollider.koljadnik.feedback;


import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface FeedbackActivityMVP {

    interface View {
        void updateActionAdapter(String[] labels);

        void updateCategoryAdapter(String[] labels);

        void showErrorToast(String text);

        String getSelectedActionText();

        String getText();

        void showSendFeedbackActivity(String[] mails, String activityTitle, String theme, String text);
    }

    interface Presenter {
        void setView(View view);

        void initData();

        void clickSendFeedbackBtn();
    }

    interface Model {
        String[] getActionsList();
        String getShowSendFeedbackActivityTitle();
        UiAction getCategoryList(OnReadListener<List<SongType>> listener);
    }
}
