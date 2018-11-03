package com.example.b00063271.safesplit.Database;

import com.example.b00063271.safesplit.Entities.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDB {
    private static final String TAG = "UserDB";
    private static final String DB_NAME = "users";
    private static FirebaseDatabase db;
    private static DatabaseReference rf;
    public static void initializeUserDB(){
         db = FirebaseDatabase.getInstance();
         rf = db.getReference(DB_NAME);
    }
    public static void addUser(User user){
        String key = rf.push().getKey();
        rf.child(key).setValue(user);
    }
    public static void deleteUser(User user){
        rf.child(user.getID()).removeValue();
    }
    public static User getUser(String userID){
        rf.child(userID);
    }

}
