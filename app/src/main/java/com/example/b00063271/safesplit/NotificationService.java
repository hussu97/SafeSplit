package com.example.b00063271.safesplit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Entities.Activities;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {

    private String userMobile;
    private ActivityDB activityDB;
    private ActivityDB.OnDatabaseInteractionListener mListener = new ActivityDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a,Activities b) {
            switch (requestCode){
                case C.CALLBACK_GET_NEW_ACTIVITY:
                    createNotification(b);
            }
        }
    };
    public NotificationService(String userMobile) {
        this.userMobile = userMobile;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mListener = new ActivityDB.OnDatabaseInteractionListener() {
            @Override
            public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a,Activities b) {
                switch (requestCode){
                    case C.CALLBACK_GET_NEW_ACTIVITY:
                        createNotification(b);
                }
            }
        };
        activityDB = new ActivityDB(mListener);
        activityDB.getNewActivity(userMobile);
    }

    private void createNotification(Activities a){
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String CHANNEL_ID = "PI";
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SafeSplit";
            String description = a.getActivityString();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else{
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("SafeSplit activity occurred")
                    .setContentTitle(a.getActivityString())
                    .setContentText("SafeSplit")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            int notificationId = 1;
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, mBuilder.build());
        }


    }

    @Override
    public void onDestroy() {
        mListener = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
