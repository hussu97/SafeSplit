package com.example.b00063271.safesplit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private final String TAG = "NetworkReceiver";
    private final ActivityDB.OnDatabaseInteractionListener mListener=null;
    private final ActivityDB activityDB = new ActivityDB(mListener);
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()) {
            activityDB.setIsConnected(true);
            context.sendBroadcast(new Intent(C.INTERNET_BROADCAST));
            Log.d(TAG, "Flag No 1");
        } else {
            Log.d(TAG, "Flag No 2");
            activityDB.setIsConnected(false);
            context.sendBroadcast(new Intent(C.NO_INTERNET_BROADCAST));
        }
    }
}
