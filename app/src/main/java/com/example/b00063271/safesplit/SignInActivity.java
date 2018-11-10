package com.example.b00063271.safesplit;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.UserDB;
import com.example.b00063271.safesplit.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupLink;
    private FirebaseAuth mAuth;
    private UserDB userDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailEditText = (EditText)findViewById(R.id.input_email);
        passwordEditText = (EditText)findViewById(R.id.input_password);
        loginButton = (Button)findViewById(R.id.btn_login);
        signupLink = (TextView)findViewById(R.id.link_signup);
        mAuth = FirebaseAuth.getInstance();

        userDB = new UserDB(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){
//            Intent intent = new Intent(this,SignOutActivity.class);
//            intent.putExtra("user",currentUser.getUid());
//            startActivity(intent);
//        }
    }
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            progressDialog.dismiss();
//                            SignInActivity.this.onLoginSuccess(user.getUid());
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            SignInActivity.this.onLoginFailed();
//                            progressDialog.dismiss();
//                        }
//
//                        // ...
//                    }
//                });
        onLoginSuccess("hi");
    }
    public void updateGetUser(User user){
        emailEditText.setText(user.getEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                String userID = intent.getStringExtra("user");
                userDB.getUser(userID);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String uID) {
        loginButton.setEnabled(true);
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user",uID);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}