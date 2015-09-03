package com.test.kogi.android6.cronometro;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.test.kogi.android6.cronometro.util.Chronometer;

public class MainActivity extends Activity {

    private Chronometer mChronometer;
    private View mActivityBackground;
    private Button mStartButton;
    private Button mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WireUpViews();

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
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mActivityBackground = findViewById(R.id.rLActHomeBackground);
        mStartButton = (Button) findViewById(R.id.bActHomeStart);
        mResetButton = (Button) findViewById(R.id.bActHomeReset);
    }

    private void ResetChrono(){
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void StartChrono(){

        if(mChronometer.isStarted()){
            mActivityBackground.setBackgroundResource(R.color.main_background_stopped);
            mChronometer.pause();
            mStartButton.setText(R.string.home_resume_button);
        }
        else{
            mActivityBackground.setBackgroundResource(R.color.main_background_running);
            mChronometer.start();
            mStartButton.setText(R.string.home_pause_button);
        }
    }

}
