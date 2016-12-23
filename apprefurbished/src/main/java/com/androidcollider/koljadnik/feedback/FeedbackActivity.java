package com.androidcollider.koljadnik.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.root.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class FeedbackActivity extends CommonToolbarActivity implements FeedbackActivityMVP.View {

    @BindView(R.id.sp_actions)
    Spinner spActions;

    @BindView(R.id.et_feedback)
    EditText etFeedback;

    @Inject
    FeedbackActivityMVP.Presenter presenter;
    private ActionsAdapter actionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        actionsAdapter = new ActionsAdapter(this, android.R.layout.simple_spinner_item);
        spActions.setAdapter(actionsAdapter);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData();
    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateActionAdapter(String[] labels) {
        actionsAdapter.update(labels);
    }

    @Override
    public String getSelectedActionText() {
        return (String) spActions.getSelectedItem();
    }

    @Override
    public String getText() {
        return etFeedback.getText().toString();
    }

    @OnClick(R.id.btn_send)
    public void onSendClick(View v) {
        presenter.clickSendFeedbackBtn();
    }

    @Override
    public void showSendFeedbackActivity(String[] mails, String activityTitle, String theme, String text) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                mails);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                (String) spActions.getSelectedItem());
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                text);
        startActivity(Intent.createChooser(emailIntent,
                text));
    }


}
