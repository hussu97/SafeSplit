package com.example.b00063271.safesplit.ProfileFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.UserDB;
import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

import androidx.constraintlayout.widget.ConstraintLayout;

public class changePasswordDialog extends Activity implements View.OnClickListener {

    private EditText passwordEditText;
    private TextView doneButton,cancelButton;

    private String userMobile;
    private UserDB userDB;
    private ActivityDB activityDB;
    private final ActivityDB.OnDatabaseInteractionListener mListener2=new ActivityDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, boolean isConnected, ArrayList<Activities> a, Activities b) {
            switch (requestCode){
                case C.CALLBACK_CHANGED_CONNECTION:
                    if(isConnected) doneButton.setEnabled(true);
                    else doneButton.setEnabled(false);
            }
        }
    };
    private UserDB.OnDatabaseInteractionListener mListener = new UserDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, String param1, String param2) {
            switch(requestCode){
                case C.CALLBACK_SET_USER_PASSWORD:
                    Toast.makeText(getApplicationContext(),"Password has been changed successfully",Toast.LENGTH_LONG).show();
                    activityDB.createActivity(userMobile,"Your -password- was changed",C.ACTIVITY_TYPE_UPDATE_PROFILE,new Date());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_password);
        passwordEditText = (EditText) findViewById(R.id.changePasswordEditText);
        doneButton = (TextView) findViewById(R.id.changePasswordDoneButton);
        cancelButton = (TextView) findViewById(R.id.changePasswordCancelButton);

        doneButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        userMobile = getIntent().getStringExtra(C.USERS_MOBILE);
        userDB = new UserDB(mListener);
        activityDB = new ActivityDB(mListener2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changePasswordCancelButton:
                finish();
                break;
            case R.id.changePasswordDoneButton:
                String password = passwordEditText.getText().toString().trim();
                if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
                    passwordEditText.setError("Password should be between 6 and 10 alphanumeric characters");
                } else {
                    userDB.setUserPassword(password);
                    finish();
                }
                break;
        }
    }
}
