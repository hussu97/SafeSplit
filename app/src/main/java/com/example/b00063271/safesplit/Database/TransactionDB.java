package com.example.b00063271.safesplit.Database;

import android.util.Log;

import com.example.b00063271.safesplit.Entities.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;

public class TransactionDB {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(C.COLLECTION_TRANSACTION);
    private CollectionReference rf_u = db.collection(C.COLLECTION_USERS);

    private double amount;
    private ArrayList<Double> moneyOweTransactions;
    private ArrayList<Double> moneyOwedTransactions;
    private ArrayList<Double> totalBalanceTransactions;
    
    private final String TAG = "TransactionDBClass";

    private OnDatabaseInteractionListener mListener;

    public TransactionDB(OnDatabaseInteractionListener mListener){
        moneyOwedTransactions = new ArrayList<>();
        moneyOweTransactions = new ArrayList<>();
        totalBalanceTransactions = new ArrayList<>();
        this.mListener = mListener;
    }

    public void getTransactions(String userMobile){
        rf_t.whereEqualTo(C.TRANSACTION_TO_ID,userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        moneyOweTransactions.clear();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble(C.TRANSACTION_AMOUNT));
                            moneyOweTransactions.add(amount);
                            totalBalanceTransactions.add(amount);
                        }
                        if(mListener!=null)
                            mListener.onDatabaseInteration(C.CALLBACK_GET_TRANSACTIONS,moneyOwedTransactions,moneyOweTransactions,totalBalanceTransactions);
                        else Log.d(TAG, "onEvent: OweCallback");
                    }
                });
        rf_t.whereEqualTo(C.TRANSACTION_FROM_ID,userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        moneyOwedTransactions.clear();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble(C.TRANSACTION_AMOUNT));
                            moneyOwedTransactions.add(amount);
                            totalBalanceTransactions.add(amount);
                        }
                        if(mListener!=null)
                            mListener.onDatabaseInteration(C.CALLBACK_GET_TRANSACTIONS,moneyOwedTransactions,moneyOweTransactions,totalBalanceTransactions);
                        else Log.d(TAG, "onEvent: OwedCallback");
                    }});
    }

    public void createTransaction(final String from,final String fromID,final String to,final String toID,final double amount){
        final DocumentReference df = rf_t.document();
        Transactions transaction = new Transactions(from,fromID,to,toID,amount);
        df.set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rf_u.document(fromID).update(C.USERS_TRANSACTIONS, FieldValue.arrayUnion(df.getId()));
                rf_u.document(toID).update(C.USERS_TRANSACTIONS, FieldValue.arrayUnion(df.getId()));
                if(mListener!=null){
                    mListener.onDatabaseInteration(C.CALLBACK_CREATE_TRANSACTION,moneyOweTransactions,moneyOwedTransactions,totalBalanceTransactions);
                }
            }
        });
    }

    public void deleteTransaction(final String userMobile,final String transactionID){
        rf_t.document(transactionID).delete();
        rf_u.document(userMobile).update(C.USERS_TRANSACTIONS,FieldValue.arrayRemove(transactionID));
    }

    public interface OnDatabaseInteractionListener {
        void onDatabaseInteration(int requestCode, ArrayList<Double> a,ArrayList<Double> b,ArrayList<Double> c);
    }
}
