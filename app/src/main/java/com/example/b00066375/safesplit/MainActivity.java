package com.example.b00066375.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button addbill;
    private Button accountsettings;
    private Button notificationsettings;
    private Button passcode;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbill = (Button) findViewById(R.id.AddBillbutton);
        accountsettings = (Button) findViewById(R.id.AccSettingsbutton);
        notificationsettings = (Button) findViewById(R.id.NotiSettingsbutton);
        passcode = (Button) findViewById(R.id.Passcodebutton);
        logout = (Button) findViewById(R.id.LogOutbutton);

        addbill.setOnClickListener(this);
        accountsettings.setOnClickListener(this);
        notificationsettings.setOnClickListener(this);
        passcode.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AddBillbutton:{
                Intent intent = new Intent(getApplicationContext(), AddUsers.class);
                startActivity(intent);
            }

        }
    }
}
