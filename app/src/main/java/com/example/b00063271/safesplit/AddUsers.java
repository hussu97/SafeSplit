package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.b00063271.safesplit.HomeScreenActivity.currentuserid;
import static com.example.b00063271.safesplit.SafeSplitApp.contactData;

// Contacts------------------------------------
//---------------------------------------------


public class AddUsers extends AppCompatActivity implements AdapterView.OnItemClickListener, MultiAutoCompleteTextView.OnEditorActionListener {


    // Data Structures
    static ArrayList<String> FRIENDS;
    static ArrayList<HashMap<String, String>> Friend_IDs;
    static ArrayList<HashMap<String, String>> users_IDs;
    private ListView friendslist;
    private MultiAutoCompleteTextView simpleMultiAutoCompleteTextView;
    private final String TAG = "add_users-";

    // For Dropdown
    String topoption = "";

    // For Backspace
    private int previousLength;
    private boolean backSpace = false;
    static boolean startingchange = false;
    static boolean checkafterchange = false;

    // For You
    static Boolean First_You = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        setContentView(R.layout.activity_add_users);

        // Initializing FRIENDS
        //------------------------------------------------------------------------------------------
        FRIENDS = new ArrayList<>();
        for(int i = 0; i < contactData.size(); i++){
            FRIENDS.add(contactData.get(i).get("name"));
        }

        // Initializing Friend_IDs
        //------------------------------------------------------------------------------------------
        Friend_IDs = new ArrayList<>();
        for(int i = 0; i < contactData.size(); i++){
            HashMap<String, String> temp = new HashMap<>();
            temp.put(contactData.get(i).get("name"), contactData.get(i).get("number"));
            Friend_IDs.add(temp);
        }

        users_IDs = new ArrayList<HashMap<String, String>>();

        if(First_You){
            HashMap<String, String> temp = new HashMap<>();
            temp.put("name", "You");
            temp.put("number", currentuserid);
            users_IDs.add(0, temp);
            First_You = false;
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


        int resource = R.layout.contacts_list;
        String[] from = {"name"};
        int[] to = {R.id.titleTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        friendslist.setAdapter(adapter);
        friendslist.setOnItemClickListener(this);
        //------------------------------------------------------------------------------------------


        //Auto Complete
        //------------------------------------------------------------------------------------------
        simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        simpleMultiAutoCompleteTextView.setDropDownBackgroundResource(R.color.colorPrimaryDark);
        final ArrayAdapter<String> friends = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, FRIENDS);
        simpleMultiAutoCompleteTextView.setAdapter(friends);

        friends.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                System.out.println("dataset changed");
                Object item = friends.getItem(0);
                topoption = item.toString();
                System.out.println("item.toString "+ item.toString());
            }
        });

        simpleMultiAutoCompleteTextView.setThreshold(1);
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        simpleMultiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startingchange = true;
                System.out.println(position + "position position position");
                HashMap<String, String> temp = new HashMap<>();

                // get the selected string from the dropdown (since it automatically gets added to the edittext)
//               String value = simpleMultiAutoCompleteTextView.getText().toString();
//               String[] data = value.split(", ");

                // the last index holds the newly entered value --> get the index of the first instance
                // of similar names in the main FRIENDS arraylist
//                String newentry = data[data.length-1];
                int nameindex = FRIENDS.indexOf(topoption);
                System.out.println("TOP OPTION: " + topoption);

                // sum of the first instance
                temp.put("name", FRIENDS.get(nameindex+position));
                temp.put("number", contactData.get(nameindex+position).get("number"));
                System.out.println("NAME: " + FRIENDS.get(nameindex+position));
                System.out.println("NUMBER: " + contactData.get(nameindex+position).get("number"));

                if (!users_IDs.contains(temp)){              // The chosen users have a similar name
                    System.out.println("ADDDDDDIINNGGGG " + FRIENDS.get(nameindex+position));
                    users_IDs.add(temp);
                }

                for (int i = 0; i < users_IDs.size(); i++)
                    System.out.println(users_IDs.get(i).get("name") + " --> " + users_IDs.get(i).get("number"));

                simpleMultiAutoCompleteTextView.setText(check());
                simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                startingchange = false;
            }
        });
        simpleMultiAutoCompleteTextView.setOnEditorActionListener(this);
        simpleMultiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index2 = simpleMultiAutoCompleteTextView.getSelectionEnd();
                if(previousLength > s.length()) backSpace = true;
                else backSpace = false;
                if(!startingchange && index2 != 0 && s.charAt(index2 - 1) == ','){
                    startingchange = true;
                    if (backSpace){
                        if(s.charAt(s.length()-1) == ',' && s.charAt(s.length()-2) == ','){
                            System.out.println("THIS ONE IS CALLED");
                            simpleMultiAutoCompleteTextView.setText(check());
                            simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                        }
                        else{
                            int index = simpleMultiAutoCompleteTextView.getSelectionEnd();
                            String value = simpleMultiAutoCompleteTextView.getText().toString();
                            if(!value.equals("")){
                                int length = index - 1;
                                int i = 0, comma_count = 0;
                                while (i < length){
                                    if(value.charAt(i) == ',') {
                                        comma_count++;
                                    }
                                    i++;
                                }
                                users_IDs.remove(comma_count);
                                System.out.println("THIS ONE IS CALLED 22");
                                simpleMultiAutoCompleteTextView.setText(check());
                                simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                                startingchange = false;
                            }
                        }
                    }
                    System.out.println("THIS ONE IS CALLED 33");
                    simpleMultiAutoCompleteTextView.setText(check());
                    simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                    startingchange = false;
                }
                else if (!startingchange && backSpace) {
                    startingchange = true;
                    int index = simpleMultiAutoCompleteTextView.getSelectionEnd();
                    String value = simpleMultiAutoCompleteTextView.getText().toString();
                    if(!value.equals("")){
                        int length = index - 1;
                        int i = 0, comma_count = 0;
                        while (i < length){
                            if(value.charAt(i) == ',') {
                                comma_count++;
                            }
                            i++;
                        }
                        if(users_IDs.size() > comma_count) users_IDs.remove(comma_count);
                        System.out.println("THIS ONE IS CALLED 44");
                        simpleMultiAutoCompleteTextView.setText(check());
                        simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                        startingchange = false;
                    }
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

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorSecondary), PorterDuff.Mode.SRC_ATOP);
            }
        }
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
                if(users_IDs.size() == 0)
                    Toast.makeText(getApplicationContext(), "Please add users!", Toast.LENGTH_SHORT).show();
                else{
                    startingchange = true;
                    simpleMultiAutoCompleteTextView.setText(check());
                    simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                    startingchange = false;
                    intent.putExtra("users", users_IDs);
                    intent.putExtra("payer", "You");
                    startActivity(intent);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        System.out.println("HEERRRRRRRREEEEEEEEEEEEEEEEEEEEEE");
        switch (v.getId()){
            case R.id.multiAutoCompleteTextView:{
                System.out.println("THIS METHOD WAS CALLEDDDDDDDDDDDDDDDDDDDDDDD");
                startingchange = true;
//                simpleMultiAutoCompleteTextView.setText(check());
                simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
                hideKeyboard(AddUsers.this);
                startingchange = false;
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
        for (int i = 1; i < users_IDs.size(); i++){
            System.out.println("ADDING " + users_IDs.get(i).get("name"));
            result += users_IDs.get(i).get("name") + ", ";
        }
        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,int position, long id){
//        simpleMultiAutoCompleteTextView.setText(check());
        String entered = simpleMultiAutoCompleteTextView.getText().toString();
        System.out.println(entered + "11111111111111111111111111111");
        HashMap<String, String> temp = new HashMap<>();

        if (entered != null) {
/*            System.out.println("case 1");
            System.out.println(entered + "22222222222222222222222222222");*/

            Boolean contains = false;
            int tempindex;

            temp.put("name", FRIENDS.get(position));
            temp.put("number", contactData.get(position).get("number"));

            if (!users_IDs.contains(temp)){              // The chosen users have a similar name
/*                contains = true;
                tempindex = users_IDs.indexOf(temp);    //must check if ID is the same --> number
                if (!Friend_IDs.get(position).get("number").equals(users_IDs.get(tempindex).get(getname()))){
                    simpleMultiAutoCompleteTextView.setText(entered + FRIENDS.get(position) + ", ");
                    users_IDs.add(temp);
                }*/
                //simpleMultiAutoCompleteTextView.setText(entered + FRIENDS.get(position) + ", ");
                users_IDs.add(temp);
            }
//            else users_IDs.add(temp);

            System.out.println(FRIENDS.get(position) + Friend_IDs.get(position).get("number"));
        }
        else {
//            System.out.println("case 2");
            Boolean contains = false;
            int tempindex;

            temp.put("name", FRIENDS.get(position));
            temp.put("number", contactData.get(position).get("number"));

            if (!users_IDs.contains(temp)){              // The chosen users have a similar name
                /*contains = true;
                tempindex = users_IDs.indexOf(temp);    //must check if ID is the same --> number
                if (!Friend_IDs.get(position).get("number").equals(users_IDs.get(tempindex).get(FRIENDS.get(position)))){
                    simpleMultiAutoCompleteTextView.setText(FRIENDS.get(position) + ", ");
                    users_IDs.add(temp);
                }*/
                //simpleMultiAutoCompleteTextView.setText(FRIENDS.get(position) + ", ");
                users_IDs.add(temp);
            }
//            else users_IDs.add(temp);

            System.out.println(FRIENDS.get(position) + Friend_IDs.get(position).get("number"));
        }

        System.out.println(entered + "444444444444444444444444444444");
        System.out.println(simpleMultiAutoCompleteTextView.getText().toString() + "==============================");
        simpleMultiAutoCompleteTextView.setText(check());
        simpleMultiAutoCompleteTextView.setSelection(simpleMultiAutoCompleteTextView.getText().length());
    }

    static public String getname(Set<String> input){
        Iterator iter = input.iterator();
        return (String)iter.next();
    }
    static public String getnumber(Set<String> input){
        Iterator iter = input.iterator();
        return (String)iter.next();
    }

}


// Should be in the list of FRIENDS - check for spellings
// name Should not already be user_IDs - check for existence

// 2things remaining:
//backspace 1 space after comma
//adding from dropdown
