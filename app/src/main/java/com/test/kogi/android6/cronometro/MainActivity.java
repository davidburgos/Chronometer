package com.test.kogi.android6.cronometro;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.test.kogi.android6.cronometro.util.Chronometer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.chronometer)
    Chronometer mChronometer;
    @Bind(R.id.rLActHomeBackground)
    View mActivityBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.bActHomeStart)
    void onStartChrono(Button StartButton){

        if(mChronometer.isStarted()){
            mActivityBackground.setBackgroundResource(R.color.main_background_stopped);
            mChronometer.pause();
            StartButton.setText(R.string.home_resume_button);
        }
        else{
            mActivityBackground.setBackgroundResource(R.color.main_background_running);
            mChronometer.start();
            StartButton.setText(R.string.home_pause_button);
        }
    }

    @OnClick(R.id.bActHomeReset)
    void onResetChrono(){
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

}