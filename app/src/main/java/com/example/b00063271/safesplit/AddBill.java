package com.example.b00063271.safesplit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class AddBill extends Activity {

    private Button paidby;
    private Button split;
    private EditText amount;
    private Spinner splitSpinner;
    private AlertDialog.Builder b;
    private ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        paidby = (Button) findViewById(R.id.paidbybutton);
        split = (Button) findViewById(R.id.splitbutton);
        amount = (EditText) findViewById(R.id.amount);

        // Get Users
        users = new ArrayList<String>();
        users.add("Custom");
        users.add("You");
        ArrayList<String> fromprev = getIntent().getStringArrayListExtra("users");
        for(int i = 0; i < fromprev.size(); i++)
            users.add(fromprev.get(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, users);
        /*
        splitSpinner = (Spinner) findViewById(R.id.splitspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splitSpinner.setAdapter(adapter);
        */

        b = new AlertDialog.Builder(this);
        b.setTitle("Paid by");
        b.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(which == 0)
                    startActivity(new Intent(getApplicationContext(), paidByActivity.class));
                else if (which == 1)
                    paidby.setText("you");
                else
                    paidby.setText(users.get(which));
            }
        });

        paidby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    b.show();
                }
            }
        });

        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });

    }
}
