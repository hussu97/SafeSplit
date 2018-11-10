package com.example.b00063271.safesplit.Database;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.SignInActivity;
import com.example.b00063271.safesplit.SignOutActivity;
import com.example.b00063271.safesplit.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserDB {
    private static final String TAG = "UserDB";
    private static final String DB_NAME = "users";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference rf = db.collection(DB_NAME);
    private SignInActivity sIn;
    private SignOutActivity sOut;
    private SignUpActivity sUp;
    public UserDB(SignInActivity context){
        this.sIn = context;
    }
    public UserDB(SignOutActivity context){
        this.sOut = context;
    }
    public UserDB(SignUpActivity context){
        this.sUp = context;
    }
    public void addUser(User user){
        Log.d(TAG, "addUser: "+user.getMobile());
        rf.document(user.getMobile()).set(user);
    }
    public void addUser(String userID, User user){
    }
    public void deleteUser(User user){

    }
    public User getUser(String userID){
        return new User();
    }
    public void getUserListener(String userID){
    }
    public void getUserList(final String userID){
    }

}
