package com.example.b00063271.safesplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.b00063271.safesplit.FriendsFragment.MainFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOweFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOwedFragment;
import com.example.b00063271.safesplit.FriendsFragment.TotalBalanceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SplitActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,DashboardFragment.OnFragmentInteractionListener
        ,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,MoneyOweFragment.OnFragmentInteractionListener, MoneyOwedFragment.OnFragmentInteractionListener,
        TotalBalanceFragment.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String userID="xJNsNNf39VJ62aiETsiO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        fragmentManager = getSupportFragmentManager();
        openFragment(new MainFragment());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.naviagation_friends:
                    return openFragment(MainFragment.newInstance(userID));
                case R.id.navigation_dashboard:
                    return openFragment(new DashboardFragment());
                case R.id.naviagtion_me:
                    return openFragment(new ProfileFragment());
                case R.id.navigation_groups:
                    return openFragment(new GroupsFragment());
                case R.id.navigation_new_bill:
                    startActivity(new Intent(getApplicationContext(),AddUsers.class));
                    overridePendingTransition(R.anim.push_bottom_up,R.anim.remain_same_position);
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
        Log.d("abcd", "onFragmentInteraction: "+uri);
    }


}
