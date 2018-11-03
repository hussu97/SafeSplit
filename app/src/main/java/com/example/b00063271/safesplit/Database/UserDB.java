package com.example.b00063271.safesplit.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.b00063271.safesplit.Entities.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDB {
    private static final String TAG = "UserDB";
    private static final String DB_NAME = "users";
    private static FirebaseDatabase db;
    private static DatabaseReference rf;
    private Context context;
    public UserDB(Context context){
        this.context = context;
    }
    public static void initializeUserDB(){
         db = FirebaseDatabase.getInstance();
         rf = db.getReference(DB_NAME);
    }
    public void addUser(User user){
        String key = rf.push().getKey();
        rf.child(key).setValue(user);
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
                //context.updateGetUser(user);
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
                context.updateGetUserListener(user);
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
                context.updateUserList(friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getUserList");
            }
        });
    }

}
