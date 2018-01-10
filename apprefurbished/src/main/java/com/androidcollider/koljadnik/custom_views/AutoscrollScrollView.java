package com.androidcollider.koljadnik.custom_views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @project Koljadnik
 */
public class AutoscrollScrollView extends ScrollView {

    private final static int PERIOD = 100;
    private int scrollSpeed;
    private Timer scrollTimer;

    public AutoscrollScrollView(Context context) {
        super(context);
    }

    public AutoscrollScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoscrollScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoscrollScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        startScroll();
    }

    private void startScroll() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer.purge();
        }
        if (scrollSpeed > 0) {
            scrollTimer = new Timer();
            scrollTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    post(() -> AutoscrollScrollView.this.scrollTo(0, AutoscrollScrollView.this.getScrollY() + 1));
                }
            }, 0, Math.round(PERIOD / scrollSpeed));
        }
    }
}
