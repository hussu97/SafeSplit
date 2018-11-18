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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.util.Iterator;

import static com.example.b00063271.safesplit.AddBill.UpdateView;
import static com.example.b00063271.safesplit.AddBill.amount;
import static com.example.b00063271.safesplit.AddBill.chosen;
import static com.example.b00063271.safesplit.AddBill.payers;
import static com.example.b00063271.safesplit.AddBill.split;

public class SplitActivity extends AppCompatActivity implements splitpercent.OnFragmentInteractionListener,splitexactamounts.OnFragmentInteractionListener,
            splitequally.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioGroup rg;
    private RadioButton rbequal, rbexact, rbpercent;
    private TextView fragment_title;
    private MainFragment.Adapter adapter;
    private splitequally equal;
    private splitexactamounts exact;
    private splitpercent percent;

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        rg = (RadioGroup) findViewById(R.id.rg_split);
        rbequal = (RadioButton) findViewById(R.id.radio0);
        rbexact = (RadioButton) findViewById(R.id.radio1);
        rbpercent = (RadioButton) findViewById(R.id.radio2);
        fragment_title = (TextView) findViewById(R.id.currentSplitTextView);

        fragmentManager = getSupportFragmentManager();
        percent = new splitpercent();
        exact = new splitexactamounts();
        equal = new splitequally();
        viewPager = (ViewPager)findViewById(R.id.splitOptionsViewPager);
        setupViewPager();

        viewPager.setCurrentItem(0, true);
        fragment_title.setText("Equal");

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio0:{
                        viewPager.setCurrentItem(0, true);
                        fragment_title.setText("Equal");
                        break;
                    }
                    case R.id.radio1:{
                        viewPager.setCurrentItem(1, true);
                        fragment_title.setText("Exact");
                        break;
                    }
                    case R.id.radio2:{
                        viewPager.setCurrentItem(2, true);
                        fragment_title.setText("Percent");
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
        adapter = new MainFragment.Adapter(fragmentManager);
        adapter.addFragment(equal, "Equal");
        adapter.addFragment(exact, "Exact");
        adapter.addFragment(percent, "Percent");
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























    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.split, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.donesplit:{

                int pageid = viewPager.getCurrentItem();
                switch (pageid){
                    case 0:{
                        equal.submit();
                        split.setText("Equally");
                        chosen = 0;
                        break;
                    }
                    case 1:{
                        exact.submit();
                        split.setText("Exact Amount");
                        chosen = 1;
                        break;
                    }
                    case 2:{
                        percent.submit();
                        split.setText("By Percent");
                        chosen = 2;
                        break;
                    }
                }
                finish();
            }
        }
        return true;
    }


}
