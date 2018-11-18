package com.example.b00063271.safesplit;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.b00063271.safesplit.DashboardFragment.DashboardFragment;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.FriendsFragment.MainFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOweFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOwedFragment;
import com.example.b00063271.safesplit.FriendsFragment.TotalBalanceFragment;
import com.example.b00063271.safesplit.ProfileFragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.prefs.Preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.example.b00063271.safesplit.AddUsers.First_You;

public class HomeScreenActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,DashboardFragment.OnFragmentInteractionListener
                ,ProfileFragment.OnFragmentInteractionListener,MoneyOwedFragment.OnFragmentInteractionListener,
        MoneyOweFragment.OnFragmentInteractionListener,TotalBalanceFragment.OnFragmentInteractionListener,View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FloatingActionButton floatingActionButton;
    private final String TAG="HSActivity";
    private String userMobile;
    private String userName;
    private SharedPreferences sharedPreferences;
    private Snackbar internetSnackbar;

    static String currentuserid;
    static String username;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Intent intent = getIntent();
        userMobile = intent.getStringExtra(C.USERS_MOBILE);
        userName = intent.getStringExtra(C.USERS_NAME);
        currentuserid = userMobile;
        username = userName;
        fragmentManager = getSupportFragmentManager();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        openFragment(MainFragment.newInstance(userMobile,userName));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_friends);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sharedPreferences = getSharedPreferences(C.LOCAL_FILE_NAME,MODE_PRIVATE);
        registerReceiver(broadcastReceiver, new IntentFilter(C.NO_INTERNET_BROADCAST));
        registerReceiver(broadcastReceiver2, new IntentFilter(C.INTERNET_BROADCAST));
        if(!isMyServiceRunning(NotificationService.class)){
            Intent serviceIntent = new Intent(this,NotificationService.class);
            serviceIntent.putExtra(C.USERS_MOBILE,userMobile);
            startService(serviceIntent);
        } else Log.d(TAG, "onCreate: Service running");
        internetSnackbar = null;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(internetSnackbar==null){
                internetSnackbar = Snackbar.make((ConstraintLayout)findViewById(R.id.homeScreenContainer),"No internet connection "+ new String(Character.toChars(0x1F62D)),Snackbar.LENGTH_INDEFINITE);
                internetSnackbar.show();
                floatingActionButton.setClickable(false);

            }
        }
    };
    BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(internetSnackbar!=null){
                internetSnackbar.dismiss();
                floatingActionButton.setClickable(true);
                internetSnackbar = null;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C.USERS_MOBILE,userMobile);
        editor.putString(C.USERS_NAME,userName);
        editor.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    Log.d(TAG, "onNavigationItemSelected: "+userMobile+" "+userName);
                    return openFragment(MainFragment.newInstance(userMobile,userName));
                case R.id.navigation_dashboard:
                    return openFragment(DashboardFragment.newInstance(userMobile,userName));
                case R.id.naviagtion_me:
                    return openFragment(ProfileFragment.newInstance(userMobile,userName));
            }
            return false;
        }
    };
    private boolean openFragment(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction: "+uri);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.floatingActionButton:
                First_You = true;
                startActivity(new Intent(getApplicationContext(),AddUsers.class));
                overridePendingTransition(R.anim.push_bottom_up,R.anim.remain_same_position);
                break;
        }
    }
}
