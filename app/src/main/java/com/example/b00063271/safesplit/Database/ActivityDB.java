package com.example.b00063271.safesplit.Database;
import android.util.Log;

import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.Entities.NotificationText;
import com.example.b00063271.safesplit.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class ActivityDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_u = db.collection(C.COLLECTION_USERS);
    private OnDatabaseInteractionListener mListener;
    private ArrayList<Activities> activities;
    private static boolean isConnected = true;
    private final String TAG = "ActDB";
    public ActivityDB(OnDatabaseInteractionListener mListener){
        this.mListener = mListener;
        activities = new ArrayList<>();
        if(mListener!=null) mListener.onDatabaseInteration(C.CALLBACK_CHANGED_CONNECTION,isConnected,null,null);
    }

    public void createActivity(String userMobile, String description, double type, Date timeStamp){
        rf_u.document(userMobile).collection(C.COLLECTION_USERS_HISTORY).add(new Activities(description,type, timeStamp));
    }
    public void setIsConnected(boolean value){
        isConnected = value;
        if(mListener!=null){
            mListener.onDatabaseInteration(C.CALLBACK_CHANGED_CONNECTION,isConnected,null,null);
        }
    }

    public void getActivity(String userMobile){
        rf_u.document(userMobile).collection(C.COLLECTION_USERS_HISTORY).orderBy(C.USERS_HISTORY_TIMESTAMP,Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        activities.clear();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            Activities a = new Activities();
                            a.setActivityString(doc.getString(C.USERS_HISTORY_ACTIVITY));
                            a.setTimeStamp(doc.getDate(C.USERS_HISTORY_TIMESTAMP));
                            int type = (int)Math.round(doc.getDouble(C.USERS_HISTORY_TYPE));
                            switch(type){
                                case C.ACTIVITY_TYPE_SETTLE_UP:
                                    a.setActivityType(R.drawable.settle_up);
                                    break;
                                case C.ACTIVITY_TYPE_UPDATE_PROFILE:
                                    a.setActivityType(R.drawable.update);
                                    break;
                                case C.ACTIVITY_TYPE_NEW_BILL:
                                    a.setActivityType(R.drawable.add_bill_dashboard);
                                    break;
                                case C.ACTIVITY_TYPE_NEW_TRANSACTION:
                                    a.setActivityType(R.drawable.owe_dashboard);
                                default:
                                    Log.d(TAG, "onEvent: Type not found");
                            }
                            activities.add(a);
                        }
                        if(mListener!= null) mListener.onDatabaseInteration(C.CALLBACK_GET_ACTIVITIES,isConnected,activities, null);
                    }
                });
    }

    public void getNewActivity(String userMobile){
        rf_u.document(userMobile).collection(C.COLLECTION_USERS_HISTORY).orderBy(C.USERS_HISTORY_TIMESTAMP,Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                     for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                         if(mListener!=null){
                             NotificationText a = new NotificationText(doc.getString(C.USERS_HISTORY_ACTIVITY),doc.getId());
                             mListener.onDatabaseInteration(C.CALLBACK_GET_NEW_ACTIVITY,isConnected,null,a);
                         }
                     }
            }
        });
    }

    public interface OnDatabaseInteractionListener {
        void onDatabaseInteration(int requestCode, boolean isConnected,ArrayList<Activities> a, NotificationText b);
    }
}
