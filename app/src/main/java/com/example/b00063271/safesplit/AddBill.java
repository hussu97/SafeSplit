package com.example.b00063271.safesplit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.C;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.b00063271.safesplit.AddUsers.users_IDs;
import static com.example.b00063271.safesplit.HomeScreenActivity.currentuserid;

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
    static ArrayList<String> users_without_custom;

    // For paying
    static HashMap<String, Float> payers;

    // For splitting
    static ArrayList<HashMap<String, String>> splittersequal;
    static ArrayList<HashMap<String, String>> splittersexact;
    static ArrayList<HashMap<String, String>> splitterspercent;
    static int chosen = 0;

    // For recording final transactions required
    static ArrayList<HashMap<String, String>> finaluserarray;
    static ArrayList<HashMap<String, String>> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        paidby = (Button) findViewById(R.id.paidbybutton);
        split = (Button) findViewById(R.id.splitbutton);
        amount = (EditText) findViewById(R.id.amount);
        description = (EditText) findViewById(R.id.description);

        // Get Users
        users = new ArrayList<String>();

        // Get Payers and Splitters
        payers = new HashMap<>();
        splittersequal = new ArrayList<>();
        splittersexact = new ArrayList<>();
        splitterspercent = new ArrayList<>();

        // Get Transactions
        finaluserarray = new ArrayList<>();
        transactions = new ArrayList<>();



        if(First){
//            payers.put("You", 0f);
            First = false;
            paidby.setText("You");

            users.add("Custom");
//            users.add("You");
        }
        else{

        }

        //ArrayList<String> fromprev = getIntent().getStringArrayListExtra("users");
        for(int i = 0; i < users_IDs.size(); i++){
            users.add(users_IDs.get(i).get("name"));
        }

        paidby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // b.show();
                    Intent dialogintent = new Intent(getApplicationContext(), paidByActivity.class);
                    dialogintent.putExtra("users", users);
                    startActivity(dialogintent);
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
                else if(!amount.getText().toString().isEmpty()){
                    current_amount = Float.parseFloat(amount.getText().toString());
                }
                else current_amount = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        users_without_custom = new ArrayList<String>();
        for (int i = 1; i < users.size(); i++){
            users_without_custom.add(users.get(i));
            payers.put(users.get(i), 0f);
        }
        for(int i = 0; i < users_without_custom.size(); i++){
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

    static void UpdateView(){
        if (payers.size() == 1){
            paidby.setText(payers.keySet().iterator().next());
        }
        else{
            paidby.setText("Custom");

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.addbill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.doneaddbill:{
                transactions.clear();
                switch (chosen){
                    case 0:{
                        System.out.println("SIZE0: " + (users_without_custom.size() == splittersequal.size()));

                        // Create the tables
                        ArrayList<Float> payed = new ArrayList<>();
                        ArrayList<Float> expense = new ArrayList<>();

/*                        System.out.println("PAYERS2: ");
                        for(Map.Entry<String, Float> entry : payers.entrySet()){
                            System.out.println(entry.getKey() + " --> " + entry.getValue());
                        }
                        System.out.println("PAYERS should be: ");
                        for(int i = 0; i < users_without_custom.size(); i++){
                            System.out.println(users_without_custom.get(i));
                        }
*/

                        for(int i = 0; i < users_without_custom.size(); i++){
                            payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
/*                            System.out.println("SIZE --> " + splittersequal.size());
                            for(int j = 0; j < splittersequal.size(); j++){
                                System.out.println("--" + splittersequal.get(j).get("name") + "--");
                                System.out.println("--" + splittersequal.get(j).get("amount") + "--");

                                System.out.println("~~" + payed.get(i) + "~~");
                            }*/
//                            System.out.println("==" + splittersequal.get(i).get("amount") + "==");
                            Float _amount_ = Float.parseFloat(splittersequal.get(i).get("amount"));
//                            System.out.println("AMOUNT: " + _amount_);
                            expense.add(Float.parseFloat(splittersequal.get(i).get("amount")) - payed.get(i)); // Get the list of expenses
                        }

                        // Find the transactions
                        while(true){

                            // Vars to store positions:
                            int positivepos = 0, negativepos = 0;

                            // Find the most positive number:
                            Float maxpositive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)){
                                    positivepos = i;
                                    maxpositive = expense.get(i);
                                }
                            }
                            if (maxpositive == 0f) break;        // All transactions evaluated !


                            // Find the most negative number:
                            Float maxnegitive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)){
                                    negativepos = i;
                                    maxnegitive = expense.get(i);
                                }
                            }
                            if (maxnegitive == 0f) break;        // All transactions evaluated !

                            // Find if pos greater or neg:
                            if (Math.abs(maxpositive) == Math.abs(maxnegitive)){
                                HashMap<String, String> temp = new HashMap<>();
                                temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                                temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                                temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                                temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                                temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                                transactions.add(temp);
                                expense.set(positivepos, 0f);
                                expense.set(negativepos, 0f);
                            }
                            else if (Math.abs(maxpositive) > Math.abs(maxnegitive)){
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
                            }
                            else {
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
                        for(int i = 1; i < users_without_custom.size(); i++){
                            HashMap<String, String> temp = new HashMap<>();
                            temp.put("name", users_without_custom.get(i));
                            temp.put("number", users_IDs.get(i-1).get("number"));
                        }
                        break;
                    }
                    case 1:{
                        System.out.println("SIZE1: " + (users_without_custom.size() == splittersexact.size()));

                        // Create the tables
                        ArrayList<Float> payed = new ArrayList<>();
                        ArrayList<Float> expense = new ArrayList<>();
                        for(int i = 0; i < users_without_custom.size(); i++){
                            payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
                            expense.add(Float.parseFloat(splittersexact.get(i).get("amount")) - payers.get(users_without_custom.get(i))); // Get the list of expenses
                        }

                        // Find the transactions
                        while(true){

                            // Vars to store positions:
                            int positivepos = 0, negativepos = 0;

                            // Find the most positive number:
                            Float maxpositive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)){
                                    positivepos = i;
                                    maxpositive = expense.get(i);
                                }
                            }
                            if (maxpositive == 0f) break;        // All transactions evaluated !


                            // Find the most negative number:
                            Float maxnegitive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)){
                                    negativepos = i;
                                    maxnegitive = expense.get(i);
                                }
                            }
                            if (maxnegitive == 0f) break;        // All transactions evaluated !

                            // Find if pos greater or neg:
                            if (Math.abs(maxpositive) == Math.abs(maxnegitive)){
                                HashMap<String, String> temp = new HashMap<>();
                                temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                                temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                                temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                                temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                                temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                                transactions.add(temp);
                                expense.set(positivepos, 0f);
                                expense.set(negativepos, 0f);
                            }
                            else if (Math.abs(maxpositive) > Math.abs(maxnegitive)){
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
                            }
                            else {
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
                        for(int i = 1; i < users_without_custom.size(); i++){
                            HashMap<String, String> temp = new HashMap<>();
                            temp.put("name", users_without_custom.get(i));
                            temp.put("number", users_IDs.get(i-1).get("number"));
                        }
                        break;
                    }
                    case 2:{
                        System.out.println("SIZE2: " + (users_without_custom.size() == splitterspercent.size()));

                        // Create the tables
                        ArrayList<Float> payed = new ArrayList<>();
                        ArrayList<Float> expense = new ArrayList<>();
                        for(int i = 0; i < users_without_custom.size(); i++){
                            payed.add(payers.get(users_without_custom.get(i)));                 // Get the list of payed amounts
                            expense.add(Float.parseFloat(splitterspercent.get(i).get("amount")) - payers.get(users_without_custom.get(i))); // Get the list of expenses
                        }

                        // Find the transactions
                        while(true){

                            // Vars to store positions:
                            int positivepos = 0, negativepos = 0;

                            // Find the most positive number:
                            Float maxpositive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) > 0 && Math.abs(expense.get(i)) > Math.abs(maxpositive)){
                                    positivepos = i;
                                    maxpositive = expense.get(i);
                                }
                            }
                            if (maxpositive == 0f) break;        // All transactions evaluated !


                            // Find the most negative number:
                            Float maxnegitive = 0f;
                            for(int i = 0; i < expense.size(); i++){
                                if(expense.get(i) < 0 && Math.abs(expense.get(i)) > Math.abs(maxnegitive)){
                                    negativepos = i;
                                    maxnegitive = expense.get(i);
                                }
                            }
                            if (maxnegitive == 0f) break;        // All transactions evaluated !

                            // Find if pos greater or neg:
                            if (Math.abs(maxpositive) == Math.abs(maxnegitive)){
                                HashMap<String, String> temp = new HashMap<>();
                                temp.put(C.TRANSACTION_FROM_ID, users_IDs.get(positivepos).get("number"));
                                temp.put(C.TRANSACTION_FROM, users_IDs.get(positivepos).get("name"));
                                temp.put(C.TRANSACTION_TO_ID, users_IDs.get(negativepos).get("number"));
                                temp.put(C.TRANSACTION_TO, users_IDs.get(negativepos).get("name"));
                                temp.put(C.TRANSACTION_AMOUNT, Float.toString(Math.abs(maxnegitive)));
                                transactions.add(temp);
                                expense.set(positivepos, 0f);
                                expense.set(negativepos, 0f);
                            }
                            else if (Math.abs(maxpositive) > Math.abs(maxnegitive)){
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
                            }
                            else {
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
                        for(int i = 1; i < users_without_custom.size(); i++){
                            HashMap<String, String> temp = new HashMap<>();
                            temp.put("name", users_without_custom.get(i));
                            temp.put("number", users_IDs.get(i-1).get("number"));
                        }
                        break;
                    }
                }

                for(int i = 0;i < transactions.size(); i++){
                    HashMap<String, String> temp = transactions.get(i);
                    System.out.println("from: " + temp.get(C.TRANSACTION_FROM_ID));
                    System.out.println("to: " + temp.get(C.TRANSACTION_TO_ID));
                    System.out.println("amount: " + temp.get(C.TRANSACTION_AMOUNT));
                    System.out.println("---------------------------------------------------");
                }

//                finish();
            }
        }
        return true;
    }

}
