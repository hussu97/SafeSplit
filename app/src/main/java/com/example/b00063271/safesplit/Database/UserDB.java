package com.example.b00063271.safesplit.Database;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.b00063271.safesplit.Entities.History;
import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.SignInActivity;
import com.example.b00063271.safesplit.SignOutActivity;
import com.example.b00063271.safesplit.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserDB {
    private static final String TAG = "UserDB";
    private static final String USER_COLLECTION = "users";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference rf = db.collection(USER_COLLECTION);
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
    public void addUser(User u){
        final User user = u;
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", user.getName());
        docData.put("mobile", user.getMobile());
        docData.put("transactionIds", user.getTransactionIds());
        docData.put("email", user.getEmail());
        docData.put("groupIds",user.getGroupIds());
        docData.put("id",user.getID());
        Log.d(TAG, "addUser: "+user.getMobile());
        rf.document(user.getMobile()).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(user.getHistories()==null) user.setHistories(new ArrayList<History>());
                rf.document(user.getMobile()).collection("history").document().set(user.getHistories());
            }
        });

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
