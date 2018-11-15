package com.example.b00063271.safesplit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.UserDB;

public class changePasswordDialog extends Activity implements View.OnClickListener {

    private EditText passwordEditText;
    private TextView doneButton,cancelButton;

    private String userMobile;
    private UserDB userDB;
    private UserDB.OnDatabaseInteractionListener mListener = new UserDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, String userEmail) {}
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
