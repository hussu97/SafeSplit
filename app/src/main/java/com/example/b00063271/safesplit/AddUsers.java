package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
// Contacts------------------------------------
//---------------------------------------------
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.b00063271.safesplit.SafeSplitApp.contactData;


public class AddUsers extends Activity implements AdapterView.OnItemClickListener, MultiAutoCompleteTextView.OnEditorActionListener {

    static ArrayList<String> FRIENDS;
    private ListView friendslist;
    private MultiAutoCompleteTextView simpleMultiAutoCompleteTextView;
    private final String TAG = "add_users-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        FRIENDS = new ArrayList<>();
        for(int i = 0; i < contactData.size(); i++){
            FRIENDS.add(contactData.get(i).get("name"));
        }
  /*
        FRIENDS.add("Alex");
        FRIENDS.add("Bob");
        FRIENDS.add("Charlie");
        FRIENDS.add("Don");
        FRIENDS.add("Edmont");
        FRIENDS.add("Flint");
        FRIENDS.add("Geaorge");
        FRIENDS.add("Harry");
        FRIENDS.add("Hussain");
        FRIENDS.add("Isaac");
        FRIENDS.add("John");
*/
        //= {"Alex","Bob","Charlie","Don","Edmond","Flint","George","Harry","Hussain","Isaac","John"};

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
        friendslist.setOnItemClickListener(this);
        //------------------------------------------------------------------------------------------


        //Auto Complete
        //------------------------------------------------------------------------------------------
        simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter<String> friends = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, FRIENDS);
        simpleMultiAutoCompleteTextView.setAdapter(friends);

        simpleMultiAutoCompleteTextView.setThreshold(1);
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        simpleMultiAutoCompleteTextView.setHint("Add to bill"); // set hint in a MultiAutoCompleteTextView
        simpleMultiAutoCompleteTextView.setOnItemClickListener(this);
        simpleMultiAutoCompleteTextView.setOnEditorActionListener(this);
        simpleMultiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = simpleMultiAutoCompleteTextView.getSelectionEnd();
                if(index != 0 && s.charAt(index - 1) == ','){
                    simpleMultiAutoCompleteTextView.setText(check());
                    simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //------------------------------------------------------------------------------------------

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.remain_same_position,R.anim.push_top_down);
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
                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                break;
            }
            case R.id.usernextmenu:{
                Intent intent = new Intent(getApplicationContext(), AddBill.class);
                simpleMultiAutoCompleteTextView.setText(check());
                simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                String value = simpleMultiAutoCompleteTextView.getText().toString();
                String[] data = value.split(", ");
                ArrayList<String> users = new ArrayList<String>();
                for (int i = 0; i < data.length; i++){
                    if (!users.contains(data[i]) && FRIENDS.contains(data[i])){
                        users.add(data[i]);
                    }
                }
                if(users.size() == 0)
                    Toast.makeText(getApplicationContext(), "Please add users!", Toast.LENGTH_SHORT).show();
                else{
                    intent.putExtra("users", users);
                    startActivity(intent);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.multiAutoCompleteTextView:{
                simpleMultiAutoCompleteTextView.setText(check());
                simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                hideKeyboard(AddUsers.this);
                return true;
            }
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String check(){
        String result = "";
        String value = simpleMultiAutoCompleteTextView.getText().toString();
        System.out.println(value + " --> value");
        String[] data = value.split(", ");
        ArrayList<String> edited = new ArrayList<String>();
        for (int i = 0; i < data.length; i++){
            if (!edited.contains(data[i]) && FRIENDS.contains(data[i])){
                System.out.println(data[i] + " entered\n----------------------\n");
                edited.add(data[i]);
                result += data[i] + ", ";
            }
        }
        //result = result.substring(0, result.length() - 2);
        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,int position, long id){
        simpleMultiAutoCompleteTextView.setText(check());
        String entered = simpleMultiAutoCompleteTextView.getText().toString();
        if (entered != null) simpleMultiAutoCompleteTextView.setText(entered + FRIENDS.get(position) + ", ");
        else simpleMultiAutoCompleteTextView.setText(FRIENDS.get(position) + ", ");
        System.out.println(entered + "-----------------------------------");
        System.out.println(simpleMultiAutoCompleteTextView.getText().toString() + "==============================");
        simpleMultiAutoCompleteTextView.setText(check());
        simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
    }

}
