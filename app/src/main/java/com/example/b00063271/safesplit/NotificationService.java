package com.example.b00063271.safesplit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.Entities.NotificationText;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {

    private String userMobile;
    private ActivityDB activityDB;
    private final String TAG = "NotifService";
    private ActivityDB.OnDatabaseInteractionListener mListener = new ActivityDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a,NotificationText b) {
            switch (requestCode){
                case C.CALLBACK_GET_NEW_ACTIVITY:
                    createNotification(b);
            }
        }
    };
    public NotificationService() {    }

    @Override
    public void onCreate() {
        super.onCreate();
        mListener = new ActivityDB.OnDatabaseInteractionListener() {
            @Override
            public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a,NotificationText b) {
                switch (requestCode){
                    case C.CALLBACK_GET_NEW_ACTIVITY:
                        createNotification(b);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userMobile = intent.getStringExtra(C.USERS_MOBILE);
        if(userMobile == ""|| userMobile ==null)
            Log.d(TAG, "onStartCommand error");
        activityDB = new ActivityDB(mListener);
        activityDB.getNewActivity(userMobile);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotification(NotificationText a){
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String CHANNEL_ID = "PI";
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SafeSplit";
            String description = a.getNotificationText();
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
                    .setContentTitle(a.getNotificationText())
                    .setContentText("SafeSplit")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            int notificationId = a.hashCode();
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
