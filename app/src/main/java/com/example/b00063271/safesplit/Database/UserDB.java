package com.example.b00063271.safesplit.Database;

import android.util.Log;

import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;

public class UserDB {
    private static final String TAG = "UserDB";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference rf_u = db.collection(C.COLLECTION_USERS);
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private OnDatabaseInteractionListener mListener;
    private String email;
    public UserDB(OnDatabaseInteractionListener mListener){
        this.mListener = mListener;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
    public void addUser(User u){
        final User user = u;
        Map<String, Object> docData = new HashMap<>();
        docData.put(C.USERS_NAME, user.getName());
        docData.put(C.USERS_MOBILE, user.getMobile());
        docData.put(C.USERS_TRANSACTIONS, user.getTransactionIds());
        docData.put(C.USERS_EMAIL, user.getEmail());
        docData.put(C.USERS_GROUPS,user.getGroupIds());
        Log.d(TAG, "addUser: "+user.getMobile());
        rf_u.document(user.getMobile()).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mListener.onDatabaseInteration(C.CALLBACK_ADD_USER,user.getMobile(),user.getName());
            }
        });

    }
    public void deleteUser(User user){

    }
    public void getUserEmail(String userMobile){
        rf_u.document(userMobile).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email = documentSnapshot.getString(C.USERS_EMAIL);
                if(mListener!=null){
                    mListener.onDatabaseInteration(C.CALLBACK_GET_USER_EMAIL,email,null);
                }
            }
        });
    }
    public void setUserEmail(final String userMobile,final String userEmail){
        mUser.updateEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(mListener!=null){
                    mListener.onDatabaseInteration(C.CALLBACK_SET_USER_EMAIL,userEmail,null);
                    rf_u.document(userMobile).update(C.USERS_EMAIL,userEmail);
                }
            }
        });
    }
    public void setUserPassword(final String userPassword){
        mUser.updatePassword(userPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(mListener!=null){
                    mListener.onDatabaseInteration(C.CALLBACK_SET_USER_PASSWORD,userPassword,null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
    public void getUserListener(String userID){
    }
    public void getUserList(final String userID){
    }

    public interface OnDatabaseInteractionListener {
        void onDatabaseInteration(int requestCode, String param1,String param2);
    }
}
