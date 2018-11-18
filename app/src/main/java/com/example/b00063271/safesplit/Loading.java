package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static com.example.b00063271.safesplit.SafeSplitApp.loading;

public class Loading extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    //sleep(10000);  //Delay of 10 seconds
                    while(loading) ;
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(Loading.this, HomeScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }}
