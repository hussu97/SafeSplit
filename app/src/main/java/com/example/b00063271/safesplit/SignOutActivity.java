package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.b00063271.safesplit.Database.BillDB;
import com.example.b00063271.safesplit.Database.UserDB;
import com.example.b00063271.safesplit.Entities.Bill;
import com.example.b00063271.safesplit.Entities.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignOutActivity extends Activity implements View.OnClickListener{

    private Button signOutBtn;
    private Button addUserBtn;
    private final String TAG = "Signout";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
        signOutBtn = (Button)findViewById(R.id.signOutBtn);
        addUserBtn = (Button)findViewById(R.id.addUser);
        addUserBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        UserDB.initializeUserDB();
        BillDB.initializeBillDB();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signOutBtn:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "Signed out");
                                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                            }
                        });
                break;
            case R.id.addUser:
                List<String>l = Arrays.asList("1","2");
               // UserDB.addUser(new User("Hussain",1234,l));
                List<String>el = Arrays.asList("1","2");
               // BillDB.addBill(new Bill(el,234.56));
        }
    }
}
