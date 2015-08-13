package com.mobile.kogi.cronometro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobile.kogi.cronometro.util.Chronometer;
import com.mobile.kogi.cronometro.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {

    private static final boolean AUTO_HIDE = false;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final boolean TOGGLE_ON_CLICK = false;

    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    private SystemUiHider mSystemUiHider;
    private Chronometer mChronometer;
    private View mActivityBackground;

    private Button mStartButton;
    private View controlsView;
    private Button mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WireUpViews();

        mSystemUiHider = SystemUiHider.getInstance(this, mChronometer, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
            // Cached values.
            int mControlsHeight;
            int mShortAnimTime;

            @Override
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
            public void onVisibilityChange(boolean visible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    // If the ViewPropertyAnimator API is available
                    // (Honeycomb MR2 and later), use it to animate the
                    // in-layout UI controls at the bottom of the
                    // screen.
                    if (mControlsHeight == 0) {
                        mControlsHeight = controlsView.getHeight();
                    }
                    if (mShortAnimTime == 0) {
                        mShortAnimTime = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                    }
                    controlsView.animate()
                            .translationY(visible ? 0 : mControlsHeight)
                            .setDuration(mShortAnimTime);
                } else {
                    // If the ViewPropertyAnimator APIs aren't
                    // available, simply show or hide the in-layout UI
                    // controls.
                    controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                if (visible && AUTO_HIDE) {
                    // Schedule a hide().
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
            }
        });

        mChronometer.setBase(SystemClock.elapsedRealtime());

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 StartChrono();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetChrono();
            }
        });

    }

    private void WireUpViews() {
        controlsView = findViewById(R.id.fullscreen_content_controls);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mActivityBackground = findViewById(R.id.activity_background);
        mStartButton = (Button) findViewById(R.id.start_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
    }

    private void ResetChrono(){
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void StartChrono(){

        if(mChronometer.isStarted()){
            mActivityBackground.setBackgroundColor(getResources().getColor(R.color.screen_background_crono_stopped));
            mChronometer.pause();
            mStartButton.setText(R.string.stop_resume);
        }
        else{
            mActivityBackground.setBackgroundColor(getResources().getColor(R.color.screen_background_crono_running));
            mChronometer.start();
            mStartButton.setText(R.string.button_pause);
        }
    }

    private void StopChrono() {
        mActivityBackground.setBackgroundResource(R.color.screen_background_crono_stopped);
        mChronometer.stop();
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fullscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mSystemUiHider.toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
