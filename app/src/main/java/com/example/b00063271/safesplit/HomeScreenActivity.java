package com.example.b00063271.safesplit;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener,DashboardFragment.OnFragmentInteractionListener
                ,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private final String TAG="HSActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        fragmentManager = getSupportFragmentManager();
        openFragment(new FriendsFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.naviagation_friends:
                    return openFragment(new FriendsFragment());
                case R.id.navigation_dashboard:
                    return openFragment(new DashboardFragment());
                case R.id.naviagtion_me:
                    return openFragment(new ProfileFragment());
                case R.id.navigation_groups:
                    return openFragment(new GroupsFragment());
                case R.id.navigation_new_bill:
                    Log.d(TAG, "onNavigationItemSelected: Entering + activity");
                    return true;
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
}
