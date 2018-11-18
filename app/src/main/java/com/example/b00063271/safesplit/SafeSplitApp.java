package com.example.b00063271.safesplit;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.b00063271.safesplit.Database.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SafeSplitApp extends Application {

    static ArrayList<HashMap<String,String>> contactData;
    static Boolean loading = true;

    @Override
    public void onCreate() {
        super.onCreate();

        // CONTACT RETRIEVAL ----------------------------------------------
        new DownloadContacts().execute();
    }

    class DownloadContacts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            contactData = new ArrayList<HashMap<String,String>>();
            ContentResolver cr = getContentResolver();
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
            while (cursor.moveToNext()) {
                try{
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                        while (phones.moveToNext()) {
                            String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber = phoneNumber.replace(" ", "").trim();
                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put("name", name);
                            map.put("number", phoneNumber);
                            contactData.add(map);
                        }
                        phones.close();
                    }
                }catch(Exception e){}
            }
            return "Downloaded";
        }
        @Override
        protected void onPostExecute(String result){
            Collections.sort(contactData, new MapComparator("name"));

            ArrayList<Integer> to_be_removed = new ArrayList<>();
            int j;
            for (int i = 0; i < contactData.size()-1; i++){
                System.out.println(i + " is the i ----------------------------------------------");
                j = 1;
                System.out.println("OUTSIDE THE LOOP--> main_name: " + contactData.get(i).get("name") + " i+j_name: " + contactData.get(i+j).get("name"));
                while (i+j < contactData.size() && contactData.get(i).get("name").equals(contactData.get(i+j).get("name"))){

                    System.out.println(i + "is still the i:");
                    System.out.println(j + " is the j and " + Integer.toString(i+j) + "is the i+j: ");
                    System.out.println("main_name: " + contactData.get(i).get("name") + " i+j_name: " + contactData.get(i+j).get("name"));
                    System.out.println("main_number: " + contactData.get(i).get("number") + " i+j_number: " + contactData.get(i+j).get("number"));

                    String number1 = C.formatNumber(contactData.get(i).get("number").trim());
                    String number2 = C.formatNumber(contactData.get(i+j).get("number").trim());
                    System.out.println(number1 + " ~~~~~ "+ number2);
                    System.out.println("--is it equal --> " + number1.equals(number2));
                    System.out.println("is it equal? --> " + contactData.get(i).get("number").equals(contactData.get(i + j).get("number")));
                    if(number1.equals(number2)){
                        System.out.println(i+j + " was added");
                        if(!to_be_removed.contains(i + j)) to_be_removed.add(i+j);
                    }
                    j++;
                }
            }
            for(int i = 0; i < to_be_removed.size(); i++) contactData.remove(to_be_removed.get(i) - i);
            for(int i = 0; i < contactData.size(); i++){
                System.out.print("Name: " + contactData.get(i).get("name"));
                System.out.println(" Phone: " + contactData.get(i).get("number"));
            }
            loading = false;
        }
    }

    class MapComparator implements Comparator<Map<String, String>> {
        private final String key;

        public MapComparator(String key){
            this.key = key;
        }

        @Override
        public int compare(Map<String, String> first, Map<String, String> second){
            String firstValue = first.get(key);
            String secondValue = second.get(key);
            return firstValue.compareTo(secondValue);
        }
    }



}
