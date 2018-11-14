package com.example.b00063271.safesplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.FriendsFragment.MainFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOweFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOwedFragment;
import com.example.b00063271.safesplit.FriendsFragment.TotalBalanceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class SplitActivity extends AppCompatActivity implements splitpercent.OnFragmentInteractionListener,splitexactamounts.OnFragmentInteractionListener,
            splitequally.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String userID="xJNsNNf39VJ62aiETsiO";
    private String username = "hussu";
    private RadioGroup rg;
    private RadioButton rbequal, rbexact, rbpercent;
    private TextView fragment_title;

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        rg = (RadioGroup) findViewById(R.id.rg_split);
        rbequal = (RadioButton) findViewById(R.id.radio0);
        rbexact = (RadioButton) findViewById(R.id.radio1);
        rbpercent = (RadioButton) findViewById(R.id.radio2);
        fragment_title = (TextView) findViewById(R.id.textView3);

        fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.splitOptionsViewPager);
        setupViewPager();

/*
        fragment_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
                rbpercent.setChecked(true);
            }
        });
*/


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio0:{
                        viewPager.setCurrentItem(0, true);
                        break;
                    }
                    case R.id.radio1:{
                        viewPager.setCurrentItem(1, true);
                        break;
                    }
                    case R.id.radio2:{
                        viewPager.setCurrentItem(2, true);
                        break;
                    }
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:{
                        rbequal.setChecked(true);
                        break;
                    }
                    case 1:{
                        rbexact.setChecked(true);
                        break;
                    }
                    case 2:{
                        rbpercent.setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(){
        MainFragment.Adapter adapter = new MainFragment.Adapter(fragmentManager);
        adapter.addFragment(splitequally.newInstance("",""), "Equal");
        adapter.addFragment(splitexactamounts.newInstance("",""), "Exact");
        adapter.addFragment(splitpercent.newInstance("",""), "Percent");
        viewPager.setAdapter(adapter);
    }
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
