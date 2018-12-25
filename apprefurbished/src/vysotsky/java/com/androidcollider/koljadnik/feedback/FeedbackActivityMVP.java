package com.androidcollider.koljadnik.feedback;


public interface FeedbackActivityMVP {

    interface View {
        void updateActionAdapter(String[] labels);

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
    }
}
