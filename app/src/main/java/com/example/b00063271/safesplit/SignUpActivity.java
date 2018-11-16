package com.example.b00063271.safesplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.UserDB;
import com.example.b00063271.safesplit.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;
    private EditText mobileEditText;
    private Button signUpButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private UserDB userDB;
    private UserDB.OnDatabaseInteractionListener mListener = new UserDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, String userMobile, String userName) {
            switch (requestCode){
                case C.CALLBACK_ADD_USER:
                    SignUpActivity.this.onSignupSuccess(userMobile,userName);
                    break;
            }
        }
    };

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameEditText = (EditText) findViewById(R.id.input_name);
        emailEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        rePasswordEditText = (EditText) findViewById(R.id.input_reEnterPassword);
        mobileEditText = (EditText) findViewById(R.id.input_mobile);
        signUpButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        mAuth = FirebaseAuth.getInstance();

        userDB = new UserDB(mListener);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signUpButton.setEnabled(false);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Creating account");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(true);
        dialog.show();

        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString();
        final String mobile = mobileEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            User user = new User(name,email,mobile,null,null);
                            userDB.addUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            SignUpActivity.this.onSignupFailed();
                        }
                    }
                });
    }
    public void onSignupSuccess(String userID,String userName) {
        signUpButton.setEnabled(true);
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra(C.USERS_MOBILE,userID);
        intent.putExtra(C.USERS_NAME,userName);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String mobile = mobileEditText.getText().toString().trim();
        String reEnterPassword = rePasswordEditText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("at least 3 characters");
            valid = false;
        } else nameEditText.setError(null);
        if (mobile.isEmpty() || mobile.length() > 8){
            mobileEditText.setError("invalid mobile number format");
            valid = false;
        } else mobileEditText.setError(null);
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else emailEditText.setError(null);
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("password must be at least 6 characters");
            valid = false;
        } else passwordEditText.setError(null);
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || !(reEnterPassword.equals(password))) {
            rePasswordEditText.setError("passwords do not match");
            valid = false;
        } else rePasswordEditText.setError(null);
        return valid;
    }
}
