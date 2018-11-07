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

import java.util.ArrayList;

public class UserDB {
    private static final String TAG = "UserDB";
    private static final String DB_NAME = "users";
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference rf = db.getReference(DB_NAME);
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
        String key = rf.push().getKey();
        rf.child(key).setValue(user);
    }
    public void addUser(String userID, User user){
        rf.child(userID).setValue(user);
    }
    public void deleteUser(User user){
        rf.child(user.getID()).removeValue();
    }
    public User getUser(String userID){
        final User[] user = {null};
        rf.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                user[0] = dataSnapshot.getValue(User.class);
                user[0].setID(key);
                sIn.updateGetUser(user[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getUser");
            }
        });
        return user[0];
    }
    public void getUserListener(String userID){
        rf.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                user.setID(key);
                //context.updateGetUserListener(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getUserListener");
            }
        });
    }
    public void getUserList(final String userID){
        rf.child(userID).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<User> friends = new ArrayList<>();
                for( DataSnapshot ds: dataSnapshot.getChildren()){
                    friends.add(getUser(ds.getKey()));
                }
                //context.updateUserList(friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getUserList");
            }
        });
    }

}
