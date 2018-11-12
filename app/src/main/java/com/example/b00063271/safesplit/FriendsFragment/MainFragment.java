package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.b00063271.safesplit.DashboardFragment;
import com.example.b00063271.safesplit.GroupsFragment;
import com.example.b00063271.safesplit.ProfileFragment;
import com.example.b00063271.safesplit.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "FriendsMainFrag";
    private final int MONEY_OWED_TAB =0;
    private final int MONEY_OWE_TAB =1;
    private final int TOTAL_BALANCE_TAB =2;

    private String userMobile;
    private String userName;

    private OnFragmentInteractionListener mListener;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabItem moneyOwedTabItem;
    private TabItem moneyOweTabItem;
    private TabItem totalBalanceTabItem;
    private ListView listView;

    private final String TRANSACTION_COLLECTION = "transaction";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf = db.collection(TRANSACTION_COLLECTION);

    private ArrayList<Double> moneyOwedTransactions;
    private ArrayList<Double> moneyOweTransactions;
    private ArrayList<Double> totalBalanceTransactions;
    private double moneyOwe=0;
    private double moneyOwed=0;
    private double totalBalance=0;

    private void getTransactions(String userMobile){
        rf.whereEqualTo("from",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                moneyOweTransactions.clear();
                moneyOwe=0;
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    double amount = Math.round(doc.getDouble("amount") * 100.0) / 100.0;
                    moneyOweTransactions.add(amount);
                    totalBalanceTransactions.add(amount);
                    moneyOwe+=amount;
                    totalBalance-=amount;
                }
                updateLists();
            }
        });
        rf.whereEqualTo("to",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                moneyOwedTransactions.clear();
                moneyOwed=0;
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    double amount = Math.round(doc.getDouble("amount") * 100.0) / 100.0;
                    moneyOwedTransactions.add(amount);
                    totalBalanceTransactions.add(amount);
                    moneyOwed+=amount;
                    totalBalance+=amount;
                }
                updateLists();
            }
                });
    }

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance(String userID,String userName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        moneyOwedTransactions=new ArrayList<>();
        moneyOweTransactions=new ArrayList<>();
        totalBalanceTransactions=new ArrayList<>();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }else{
            userMobile = "12345";
            userName = "Hussu";
        }
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getTransactions(userMobile);
        View view = inflater.inflate(R.layout.fragment_friends_main,container, false);
        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.friends_view_pager);
        listView = (ListView) view.findViewById(R.id.money_owed_listview);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabLayout = (TabLayout) view.findViewById(R.id.friends_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        moneyOwedTabItem = (TabItem)view.findViewById(R.id.owed_tab_item);
        return view;

    }

    private void updateLists(){
        totalBalanceTransactions.clear();
        totalBalance=0;
        for(double amt:moneyOweTransactions){
            amt=-1*amt;
            totalBalanceTransactions.add(amt);
            totalBalance+=amt;
        }
        for(double amt:moneyOwedTransactions){
            totalBalanceTransactions.add(amt);
            totalBalance+=amt;
        }
        tabLayout.getTabAt(MONEY_OWED_TAB).setText("Money Owed "+moneyOwed);
        tabLayout.getTabAt(MONEY_OWE_TAB).setText("Money Owe "+moneyOwe);
        tabLayout.getTabAt(TOTAL_BALANCE_TAB).setText("Total balance "+totalBalance);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(MoneyOwedFragment.newInstance(userMobile,userName), "Money Owed");
        adapter.addFragment(MoneyOweFragment.newInstance(userMobile), "Money Owe");
        adapter.addFragment(TotalBalanceFragment.newInstance(userMobile), "Total balance");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public Adapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
