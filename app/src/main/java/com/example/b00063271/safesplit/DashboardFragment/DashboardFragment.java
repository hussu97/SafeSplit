package com.example.b00063271.safesplit.DashboardFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    private final String TAG = "DashboardFrag";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_u = db.collection(C.COLLECTION_USERS);

    private String userMobile;
    private String userName;
    private TextView noDashboard;
    private ActivityDB activityDB;
    private final ActivityDB.OnDatabaseInteractionListener mDBListener=new ActivityDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a, Activities b) {
            switch (requestCode){
                case C.CALLBACK_GET_ACTIVITIES:
                    updateList(a);
            }
        }
    };

    private ArrayList<Activities> activities;
    private ListView dashboardListView;
    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activityDB = new ActivityDB(mDBListener);
        activityDB.getActivity(userMobile);
        noDashboard = v.findViewById(R.id.noDashboardTextView);
        dashboardListView = (ListView) v.findViewById(R.id.dashboardListView);
        return v;
    }

    private void updateList(ArrayList<Activities> a) {
        if(activities.size()!=0) {
            noDashboard.setVisibility(View.GONE);
            dashboardListView.setAdapter(new CustomAdapter(this, activities));
        }
        else noDashboard.setVisibility(View.VISIBLE);
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
    public interface OnFragmentInteractionListener { void onFragmentInteraction(Uri uri); }
}
