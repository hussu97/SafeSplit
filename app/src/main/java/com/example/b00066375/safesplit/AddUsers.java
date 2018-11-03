package com.example.b00066375.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddUsers extends Activity {

    final static String[] FRIENDS = {"Alex","Bob","Charlie","Don","Edmond","Flint","George","Harry","Hussain","Isaac","John"};
    private ListView friendslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        //List View
        //------------------------------------------------------------------------------------------
        friendslist = (ListView) findViewById(R.id.friendslistview);

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (String friend:FRIENDS){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", friend);
            data.add(map);
        }


        int resource = R.layout.list_item;
        String[] from = {"name"};
        int[] to = {R.id.titleTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        friendslist.setAdapter(adapter);
        //------------------------------------------------------------------------------------------


        //Auto Complete
        //------------------------------------------------------------------------------------------
        MultiAutoCompleteTextView simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter<String> friends = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, FRIENDS);
        simpleMultiAutoCompleteTextView.setAdapter(friends);

        simpleMultiAutoCompleteTextView.setThreshold(1);
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        simpleMultiAutoCompleteTextView.setHint("Add to bill"); // set hint in a MultiAutoCompleteTextView
        simpleMultiAutoCompleteTextView.setValidator(new Validator());
        simpleMultiAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ((MultiAutoCompleteTextView)textView).performValidation();
                return true;
            }
        });
        //------------------------------------------------------------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.adduser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.userprevmenu:{
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            case R.id.usernextmenu:{
                Intent intent = new Intent(getApplicationContext(), AddBill.class);
                intent.putExtra("friends", FRIENDS);
                startActivity(intent);
            }
        }
    }

    class Validator implements MultiAutoCompleteTextView.Validator{

        @Override
        public boolean isValid(CharSequence charSequence) {
            Log.d("Test", "Checking if valid: "+ charSequence);
            Arrays.sort(FRIENDS);
            if (Arrays.binarySearch(FRIENDS, charSequence.toString()) > 0) {
                return true;
            }

            return false;        }

        @Override
        public CharSequence fixText(CharSequence charSequence) {
            Log.v("Test", "Returning fixed text");

            return "";
        }
    }
}
