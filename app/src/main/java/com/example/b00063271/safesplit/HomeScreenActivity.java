package com.example.b00063271.safesplit;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.naviagation_friends:
                    FriendsFragment fragment = new FriendsFragment();
                    fragmentTransaction.add(R.id.frame_container, fragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("dashboard");
                    return true;
                case R.id.naviagtion_me:
                    mTextMessage.setText("me");
                    return true;
                case R.id.navigation_groups:
                    mTextMessage.setText("groups");
                    return true;
                case R.id.navigation_new_bill:
                    mTextMessage.setText("new");
                    return true;
            }
            return false;
        }
    };



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
