package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import static com.example.b00063271.safesplit.SafeSplitApp.loading;

public class Loading extends Activity {

    private ImageView logo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        logo = (ImageView) findViewById(R.id.logoImgView);
        ScaleAnimation scaler = new ScaleAnimation((float) 0.7, (float) 1.0, (float) 0.7, (float) 1.0);
        scaler.setDuration(40);
        logo.startAnimation(scaler);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    //sleep(10000);  //Delay of 10 seconds

                    while(loading) ;
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(Loading.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }}
