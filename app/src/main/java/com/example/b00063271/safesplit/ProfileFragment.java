package com.example.b00063271.safesplit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.UserDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private Button changeEmailButton;
    private Button changePasswordButton;
    private Button signOutButton;

    private UserDB userDB;
    private String userEmail, userMobile, userName;
    private UserDB.OnDatabaseInteractionListener mDBListener = new UserDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, String userEmail) {
            if(requestCode== C.CALLBACK_GET_USER_EMAIL){
                userEmailTextView.setText(userEmail);
            }
        }
    };

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
        userDB = new UserDB(mDBListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        userEmailTextView = (TextView) v.findViewById(R.id.userEmailProfileTextView);
        userNameTextView = (TextView) v.findViewById(R.id.userNameProfileTextView);
        changeEmailButton = (Button)v.findViewById(R.id.changeEmailButton);
        changePasswordButton = (Button)v.findViewById(R.id.changePasscodeButton);
        signOutButton = (Button)v.findViewById(R.id.signOutButton);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userDB.getUserEmail(userMobile);

        userNameTextView.setText(userName);

        changeEmailButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        return v;
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.changeEmailButton:
                Intent intent = new Intent(getActivity(),changeEmailDialog.class);
                intent.putExtra(C.USERS_MOBILE,userMobile);
                startActivity(intent);
                break;
            case R.id.changePasscodeButton:
                Intent intent3 = new Intent(getActivity(),changeEmailDialog.class);
                intent3.putExtra(C.USERS_MOBILE,userMobile);
                startActivity(intent3);
                break;
            case R.id.signOutButton:
                mAuth.signOut();
                Intent intent2 = new Intent(getActivity(),SignInActivity.class);
                intent2.putExtra(C.USERS_MOBILE,userMobile);
                startActivity(intent2);
                break;

        }
    }

    public interface OnFragmentInteractionListener { void onFragmentInteraction(Uri uri); }
}
