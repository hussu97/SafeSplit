package com.example.b00063271.safesplit.Database;

import com.example.b00063271.safesplit.Entities.Activities;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ActivityDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_u = db.collection(C.COLLECTION_USERS);

    public ActivityDB(){}

    public void createActivity(String userMobile, String description, double type, Date timeStamp){
        rf_u.document(userMobile).collection(C.COLLECTION_USERS_HISTORY).add(new Activities(description,type, timeStamp));
    }

}
