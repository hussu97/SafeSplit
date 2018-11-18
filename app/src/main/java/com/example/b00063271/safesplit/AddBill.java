package com.example.b00063271.safesplit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.TransactionDB;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.b00063271.safesplit.AddUsers.users_IDs;
import static com.example.b00063271.safesplit.HomeScreenActivity.currentuserid;
import static com.example.b00063271.safesplit.HomeScreenActivity.username;

import java.lang.Math.*;
import java.util.Map;

public class AddBill extends AppCompatActivity {

    // PRIVATES
    private Spinner splitSpinner;
    private AlertDialog.Builder b;

    // STATICS
    static Button paidby;
    static Button split;
    static EditText amount;
    static EditText description;
    private Boolean First = true;

    // Current Sum Total and Description
    static float current_amount;
    private String description_text;

    // For recording users who are involved in the bill
    static ArrayList<String> users;
    static ArrayList<String> numbers;
    static ArrayList<String> users_without_custom;

    // For paying
    static HashMap<String, Float> payers;

    // For splitting
    static ArrayList<HashMap<String, String>> splittersequal;
    static ArrayList<HashMap<String, String>> splittersexact;
    static ArrayList<HashMap<String, String>> splitterspercent;
    static Boolean Default_split = true;
    static Boolean Default_payer = true;
    static int chosen = 0;

    // For recording final transactions required
    static ArrayList<HashMap<String, String>> finaluserarray;
    static ArrayList<HashMap<String, String>> transactions;

    private TransactionDB transactionDB;
    private ActivityDB activityDB;
    private ActivityDB.OnDatabaseInteractionListener mListener2 = null;
    private TransactionDB.OnDatabaseInteractionListener mListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        Default_payer = true;
        Default_split = true;

        paidby = (Button) findViewById(R.id.paidbybutton);
        split = (Button) findViewById(R.id.splitbutton);
        amount = (EditText) findViewById(R.id.amount);
        description = (EditText) findViewById(R.id.description);

        transactionDB = new TransactionDB(mListener);
        activityDB = new ActivityDB(mListener2);

        // Get Users
        users = new ArrayList<String>();
        numbers = new ArrayList<>();

        // Get Payers and Splitters
        payers = new HashMap<>();
        splittersequal = new ArrayList<>();
        splittersexact = new ArrayList<>();
        splitterspercent = new ArrayList<>();

        // Get Transactions
        finaluserarray = new ArrayList<>();
        transactions = new ArrayList<>();


        if (First) {
//            payers.put("You", 0f);
            First = false;
            paidby.setText("You");

            users.add("Custom");
            numbers.add("");
//            users.add("You");
        } else {

        }

        //ArrayList<String> fromprev = getIntent().getStringArrayListExtra("users");
        for (int i = 0; i < users_IDs.size(); i++) {
            users.add(users_IDs.get(i).get("name"));
            numbers.add(users_IDs.get(i).get("number"));
            //payers.put(users_IDs.get(i).get("name"), 0f);
        }

        paidby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()) {
                    AddBill.this.showEmptyBillDialog();
                } else {
                    // b.show();
                    Intent dialogintent = new Intent(getApplicationContext(), paidByActivity.class);
                    dialogintent.putExtra("users", users);
                    dialogintent.putExtra("number",numbers);
                    startActivity(dialogintent);
                }
            }
        });

        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()) {
                    AddBill.this.showEmptyBillDialog();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SplitActivity.class);
                    startActivity(intent);
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(description.getText().toString().isEmpty()) description_text = "";
                else description_text = description.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (amount.getText().toString().equals(".")) current_amount = 0;
                else if (!amount.getText().toString().isEmpty()) {
                    current_amount = Float.parseFloat(amount.getText().toString());
                } else current_amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        users_without_custom = new ArrayList<String>();
        for (int i = 1; i < users.size(); i++) {
            users_without_custom.add(users.get(i));
            payers.put(users.get(i), 0f);
        }
//        payers.put("You", current_amount);
        for (int i = 0; i < users_without_custom.size(); i++) {
            HashMap<String, String> splitter = new HashMap<>();
            splitter.put("name", users_without_custom.get(i));
            splitter.put("amount", Float.toString(0f));
            splittersequal.add(splitter);
            splittersexact.add(splitter);
            splitterspercent.add(splitter);
        }
        System.out.println("PAYERS: ");
        for(Map.Entry<String, Float> entry : payers.entrySet()){
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }


    }


    static void UpdateView(String who) {
        paidby.setText(who);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addbill, menu);
        return true;
    }

    private void showDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(C.SETTLE_UP)
                .setMessage("Are you sure you want to create this bill")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addBill();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showEmptyBillDialog(){
        C.buildDialog(this,"Please enter an amount!");
    }
    private void addBill() {
        transactions.clear();
        if(Default_split){
            for(int i = 0; i < users_without_custom.size(); i++){
                HashMap<String, String> splitter = new HashMap<>();
                splitter.put("name", users_without_custom.get(i));
                splitter.put("amount", Float.toString(current_amount/users_without_custom.size()));
                splittersequal.set(i,splitter);
            }
        }
        if(Default_payer){
            for (int i = 1; i < users.size(); i++) {
                if(i == 1) payers.put(users.get(i), current_amount);
                else payers.put(users.get(i), 0f);
            }
        }
        switch (chosen) {
            case 0: {
                System.out.println("SIZE0: " + (users_without_custom.size() == splittersequal.size()));

                // Create the tables
                ArrayList<Float> payed = new ArrayList<>();
                ArrayList<Float> expense = new ArrayList<>();
                for (int i = 0; i < users_without_custom.size(); i++) {
                    payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
                    expense.add(Float.parseFloat(splittersequal.get(i).get("amount")) - payers.get(users_without_custom.get(i))); // Get the list of expenses
                }                // Find the transactions
                while (true) {

                    // Vars to store positions:
                    int positivepos = 0, negativepos = 0;

                    // Find the most positive number:
                    Float maxpositive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)) {
                            positivepos = i;
                            maxpositive = expense.get(i);
                        }
                    }
                    if (maxpositive == 0f) break;        // All transactions evaluated !


                    // Find the most negative number:
                    Float maxnegitive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)) {
                            negativepos = i;
                            maxnegitive = expense.get(i);
                        }
                    }
                    if (maxnegitive == 0f) break;        // All transactions evaluated !

                    // Find if pos greater or neg:
                    if (Math.abs(maxpositive) == Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, 0f);
                    } else if (Math.abs(maxpositive) > Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxnegitive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, maxnegitive + maxpositive);
                        expense.set(negativepos, 0f);
                    } else {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxpositive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, maxnegitive + maxpositive);
                    }

                }

                // Get the name/id set:
                for (int i = 1; i < users_without_custom.size(); i++) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("name", users_without_custom.get(i));
                    temp.put("number", users_IDs.get(i - 1).get("number"));
                }
                break;
            }
            case 1: {
                System.out.println("SIZE1: " + (users_without_custom.size() == splittersexact.size()));

                // Create the tables
                ArrayList<Float> payed = new ArrayList<>();
                ArrayList<Float> expense = new ArrayList<>();
                for (int i = 0; i < users_without_custom.size(); i++) {
                    payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
                    expense.add(Float.parseFloat(splittersexact.get(i).get("amount")) - payers.get(users_without_custom.get(i))); // Get the list of expenses
                }

                // Find the transactions
                while (true) {

                    // Vars to store positions:
                    int positivepos = 0, negativepos = 0;

                    // Find the most positive number:
                    Float maxpositive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)) {
                            positivepos = i;
                            maxpositive = expense.get(i);
                        }
                    }
                    if (maxpositive == 0f) break;        // All transactions evaluated !


                    // Find the most negative number:
                    Float maxnegitive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)) {
                            negativepos = i;
                            maxnegitive = expense.get(i);
                        }
                    }
                    if (maxnegitive == 0f) break;        // All transactions evaluated !

                    // Find if pos greater or neg:
                    if (Math.abs(maxpositive) == Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, 0f);
                    } else if (Math.abs(maxpositive) > Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxnegitive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, maxnegitive + maxpositive);
                        expense.set(negativepos, 0f);
                    } else {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxpositive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, maxnegitive + maxpositive);
                    }

                }

                // Get the name/id set:
                for (int i = 1; i < users_without_custom.size(); i++) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("name", users_without_custom.get(i));
                    temp.put("number", users_IDs.get(i - 1).get("number"));
                }
                break;
            }
            case 2: {
                System.out.println("SIZE2: " + (users_without_custom.size() == splitterspercent.size()));

                // Create the tables
                ArrayList<Float> payed = new ArrayList<>();
                ArrayList<Float> expense = new ArrayList<>();
                for (int i = 0; i < users_without_custom.size(); i++) {
                    payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
                    expense.add(Float.parseFloat(splitterspercent.get(i).get("amount")) - payers.get(users_without_custom.get(i))); // Get the list of expenses
                }

                // Find the transactions
                while (true) {

                    // Vars to store positions:
                    int positivepos = 0, negativepos = 0;

                    // Find the most positive number:
                    Float maxpositive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)) {
                            positivepos = i;
                            maxpositive = expense.get(i);
                        }
                    }
                    if (maxpositive == 0f) break;        // All transactions evaluated !


                    // Find the most negative number:
                    Float maxnegitive = 0f;
                    for (int i = 0; i < expense.size(); i++) {
                        if (expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)) {
                            negativepos = i;
                            maxnegitive = expense.get(i);
                        }
                    }
                    if (maxnegitive == 0f) break;        // All transactions evaluated !

                    // Find if pos greater or neg:
                    if (Math.abs(maxpositive) == Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, 0f);
                    } else if (Math.abs(maxpositive) > Math.abs(maxnegitive)) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxnegitive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, maxnegitive + maxpositive);
                        expense.set(negativepos, 0f);
                    } else {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                        temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                        temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                        temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                        Float amount = Math.abs(maxpositive);
                        temp.put(C.TRANSACTION_AMOUNT, Float.toString(amount));
                        transactions.add(temp);
                        expense.set(positivepos, 0f);
                        expense.set(negativepos, maxnegitive + maxpositive);
                    }

                }

                // Get the name/id set:
                for (int i = 1; i < users_without_custom.size(); i++) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("name", users_without_custom.get(i));
                    temp.put("number", users_IDs.get(i - 1).get("number"));
                }
                break;
            }
        }

        for (int i = 0; i < transactions.size(); i++) {
            HashMap<String, String> temp = transactions.get(i);
            System.out.println("from: " + temp.get(C.TRANSACTION_FROM_ID));
            System.out.println("to: " + temp.get(C.TRANSACTION_TO_ID));
            System.out.println("amount: " + temp.get(C.TRANSACTION_AMOUNT));
            System.out.println("---------------------------------------------------");
        }
        startHomeScreen();
    }

    private void startHomeScreen(){
        for (HashMap<String, String> i : transactions) {
            if (i.get(C.TRANSACTION_TO) == "You")
                transactionDB.createTransaction(username, C.formatNumber(i.get(C.TRANSACTION_TO_ID)), C.formatNumber(i.get(C.TRANSACTION_FROM)), C.formatNumber(i.get(C.TRANSACTION_FROM_ID)), C.round(Double.valueOf(i.get(C.TRANSACTION_AMOUNT))));
            else if (i.get(C.TRANSACTION_FROM) == "You")
                transactionDB.createTransaction(i.get(C.TRANSACTION_TO), C.formatNumber(i.get(C.TRANSACTION_TO_ID)), username, C.formatNumber(i.get(C.TRANSACTION_FROM_ID)), C.round(Double.valueOf(i.get(C.TRANSACTION_AMOUNT))));
            else
                transactionDB.createTransaction(i.get(C.TRANSACTION_TO), C.formatNumber(i.get(C.TRANSACTION_TO_ID)), i.get(C.TRANSACTION_FROM), C.formatNumber(i.get(C.TRANSACTION_FROM_ID)), C.round(Double.valueOf(i.get(C.TRANSACTION_AMOUNT))));
            activityDB.createActivity(C.formatNumber(i.get(C.TRANSACTION_FROM_ID)), "You are owed -" + C.round(Double.valueOf(i.get(C.TRANSACTION_AMOUNT))) + "- from " + i.get(C.TRANSACTION_FROM), C.ACTIVITY_TYPE_NEW_TRANSACTION, new Date());
            activityDB.createActivity(C.formatNumber(i.get(C.TRANSACTION_TO_ID)), "You owe -" + C.round(Double.valueOf(i.get(C.TRANSACTION_AMOUNT))) + "- to " + i.get(C.TRANSACTION_FROM), C.ACTIVITY_TYPE_NEW_TRANSACTION, new Date());
        }
        for(HashMap<String,String> j : users_IDs){
            if(description_text.isEmpty())
                activityDB.createActivity(j.get("number"),"A new bill of -"+C.round(current_amount)+"- AED has been added",C.ACTIVITY_TYPE_NEW_BILL,new Date());
            else
                activityDB.createActivity(j.get("number"),"A new bill -"+description_text+" ("+C.round(current_amount)+" AED)- has been added",C.ACTIVITY_TYPE_NEW_BILL,new Date());
        }
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra(C.USERS_MOBILE, currentuserid);
        intent.putExtra(C.USERS_NAME, username);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneaddbill: {
                if (amount.getText().toString().isEmpty()) {
                    showEmptyBillDialog();
                }
                else showDialog();
            }

        }
        return true;
    }
}
