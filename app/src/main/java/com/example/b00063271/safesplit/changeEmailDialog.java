package com.example.b00063271.safesplit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.UserDB;

public class changeEmailDialog extends Activity implements View.OnClickListener {

    private EditText emailEditText;
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
        setContentView(R.layout.dialog_change_email);
        emailEditText = (EditText) findViewById(R.id.changeEmailEditText);
        doneButton = (TextView) findViewById(R.id.changeEmailDoneButton);
        cancelButton = (TextView) findViewById(R.id.changeEmailCancelButton);

        doneButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        userMobile = getIntent().getStringExtra(C.USERS_MOBILE);
        userDB = new UserDB(mListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeEmailCancelButton:
                finish();
                break;
            case R.id.changeEmailDoneButton:
                String email = emailEditText.getText().toString().trim();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Enter a valid email address");
                } else {
                    userDB.setUserEmail(userMobile,email);
                    finish();
                }
                break;
        }
    }
}
