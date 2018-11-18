package com.example.b00063271.safesplit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.C;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.b00063271.safesplit.AddBill.Default_payer;
import static com.example.b00063271.safesplit.AddBill.UpdateView;
import static com.example.b00063271.safesplit.AddBill.amount;
import static com.example.b00063271.safesplit.AddBill.payers;

public class CustomPayment extends AppCompatActivity {

    ListView participants;
    ArrayList<String> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("1 ------------------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_payment);

        participants = (ListView) findViewById(R.id.custom_list);
        users = getIntent().getStringArrayListExtra("users");
        users.remove(0);        // remove "custom"




        ArrayList<HashMap<String, Object>> namedata = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < users.size(); i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", users.get(i));
            if (payers.get(users.get(i)) == 0f)
                map.put("amount", "");
            else
                map.put("amount", payers.get(users.get(i)));
            namedata.add(map);
            System.out.println(users.get(i) + " was added");
        }


        int resource = R.layout.custom_payment_item;
        String[] from = {"name", "amount"};
        int[] to = {R.id.user_participant, R.id.payed_amount};

        SimpleAdapter adapter = new SimpleAdapter(this, namedata, resource, from, to);
        participants.setAdapter(adapter);

        System.out.println("1111" + users.size());
        System.out.println("2 ------------------------------------------------");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.custompayment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.donecustompayment:{
                Float sum = 0f;
                //payers.clear();
                for (int i = 0; i < users.size(); i++){
                    View wantedView = participants.getChildAt(i);
                    EditText individual_ED = (EditText) wantedView.findViewById(R.id.payed_amount);
                    Float individual_amount;
                    if (individual_ED.getText() != null && !individual_ED.getText().toString().equals("") && Float.parseFloat(individual_ED.getText().toString()) != 0f){
                        individual_amount = Float.parseFloat(individual_ED.getText().toString());
                        sum += individual_amount;
                        payers.put(users.get(i), individual_amount);
                        System.out.println(users.get(i) + " added to the list --------------------");
                    }
                    else payers.put(users.get(i), 0f);
                }
                Float currAmount = Float.parseFloat(amount.getText().toString());
                if (sum < currAmount){
                    C.buildDialog(this,"You still have "+ Double.valueOf(C.round(currAmount - sum))+" AED remaining");
                }
                else if (sum > currAmount){
                    C.buildDialog(this,"You need to remove "+ Double.valueOf(C.round(sum - currAmount))+" AED");
                }

                else{
                    Iterator itt = payers.keySet().iterator();
                    System.out.println(payers.size() + "===========================================");
                    for (int i = 0; i < payers.size(); i++){
                        String key = (String)itt.next();
                        System.out.print(key);
                        System.out.print(" ==> ");
                        System.out.println(payers.get(key));
                    }
                    System.out.println("===========================================");
                    Default_payer = false;
                    int checkcustom = 0;
                    Boolean check_custom = false;
                    String ifnotcustom = "";
                    for(int k = 0; k < payers.size(); k++){
                        if(payers.get(users.get(k)) != 0f) {
                            ifnotcustom = users.get(k);
                            checkcustom++;
                        }
                        if(checkcustom>1)check_custom = true;
                        //UpdateView("Custom");
                    }
                    if(check_custom) UpdateView("Custom");
                    else UpdateView(ifnotcustom);
                    finish();
                }
            }
        }
        return true;
    }

    public void defaultcall(){
        System.out.println("1 =================================================");

        System.out.println("2222" + users.size());
        if (payers.size() > 1) {
            for (int i = 0; i < users.size(); i++) {
                View wantedView = participants.getChildAt(i);
                System.out.println("5 defCakk=============================================="+participants.getChildCount());
                TextView individual_TV = (TextView) wantedView.findViewById(R.id.user_participant);
                EditText individual_ED = (EditText) wantedView.findViewById(R.id.payed_amount);
                String who = individual_TV.getText().toString();
                if(payers.containsKey(who)) individual_ED.setText(payers.get(who).toString());
            }
        }

        System.out.println("2 =================================================");

    }

}
