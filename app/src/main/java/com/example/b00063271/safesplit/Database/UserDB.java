package com.example.b00063271.safesplit.Database;

import android.media.MediaDrm;
import android.util.Log;

import com.example.b00063271.safesplit.Entities.History;
import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.SignInActivity;
import com.example.b00063271.safesplit.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
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
    private SignInActivity sIn;
    private SignUpActivity sUp;

    private OnDatabaseInteractionListener mListener;
    private String email;
    public UserDB(OnDatabaseInteractionListener mListener){
        this.mListener = mListener;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
    public UserDB(SignInActivity context){
        this.sIn = context;
    }
    public UserDB(SignUpActivity context){
        this.sUp = context;
    }
    public void addUser(User u){
        final User user = u;
        Map<String, Object> docData = new HashMap<>();
        docData.put(C.USERS_NAME, user.getName());
        docData.put(C.USERS_MOBILE, user.getMobile());
        docData.put(C.USERS_TRANSACTIONS, user.getTransactionIds());
        docData.put(C.USERS_EMAIL, user.getEmail());
        docData.put(C.USERS_GROUPS,user.getGroupIds());
        docData.put("id",user.getID());
        Log.d(TAG, "addUser: "+user.getMobile());
        rf_u.document(user.getMobile()).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(user.getHistories()==null) user.setHistories(new ArrayList<History>());
                rf_u.document(user.getMobile()).collection(C.COLLECTION_USERS_HISTORY).document().set(user.getHistories());
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
                    mListener.onDatabaseInteration(C.CALLBACK_GET_USER_EMAIL,email);
                }
            }
        });
    }
    public void setUserEmail(String userMobile, String userEmail){
        mUser.updateEmail(userEmail);
        rf_u.document(userMobile).update(C.USERS_EMAIL,userEmail);
    }
    public void getUserListener(String userID){
    }
    public void getUserList(final String userID){
    }

    public interface OnDatabaseInteractionListener {
        void onDatabaseInteration(int requestCode, String userEmail);
    }
}
