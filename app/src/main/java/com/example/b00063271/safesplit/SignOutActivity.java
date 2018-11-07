package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b00063271.safesplit.Database.BillDB;
import com.google.firebase.auth.FirebaseAuth;

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

        BillDB.initializeBillDB();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signOutBtn:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.addUser:
                List<String>l = Arrays.asList("1","2");
               // UserDB.addUser(new User("Hussain",1234,l));
                List<String>el = Arrays.asList("1","2");
               // BillDB.addBill(new Bill(el,234.56));
        }
    }
}
