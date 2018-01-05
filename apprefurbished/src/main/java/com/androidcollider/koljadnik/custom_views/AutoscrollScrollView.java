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
            int period = scrollSpeed < 5 ? 100 : 50;
            final int speed = scrollSpeed < 5 ? scrollSpeed : scrollSpeed / 2;
            scrollTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    AutoscrollScrollView.this.scrollTo(0, AutoscrollScrollView.this.getScrollY() + speed);
                }
            }, 0, period);
        }
    }
}
