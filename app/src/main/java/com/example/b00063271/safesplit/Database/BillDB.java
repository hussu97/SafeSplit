package com.example.b00063271.safesplit.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.b00063271.safesplit.Entities.Bill;
import com.example.b00063271.safesplit.Entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillDB {
    private static final String DB_NAME = "bills";
    private static final String TAG = "BillDB";

    private static FirebaseDatabase db;
    private static DatabaseReference rf;
    private Context context;
    public BillDB(Context context){
        this.context = context;
    }

    public static void initializeBillDB(){
        db = FirebaseDatabase.getInstance();
        rf = db.getReference(DB_NAME);
    }
    public void addBill(Bill bill){
        String key = rf.push().getKey();
        rf.child(key).setValue(bill);
    }
    public void addBill(double billAmt, List<String> userIds){
        addBill(new Bill(userIds,billAmt));
    }
    public void deleteBill(Bill bill){
        rf.child(bill.getID()).removeValue();
    }
    public Bill getBill(String billID){
        final Bill[] bill = {null};
        rf.child(billID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                bill[0] = dataSnapshot.getValue(Bill.class);
                bill[0].setID(key);
                //context.updateGetUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getBill");
            }
        });
        return bill[0];
    }
    public void getBillListener(String userID){
        rf.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Bill bill = dataSnapshot.getValue(Bill.class);
                bill.setID(key);
                context.updateGetBillListener(bill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getBillListener");
            }
        });
    }
    public void getBillUsersList(final String userID){
        rf.child(userID).child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Bill> friends = new ArrayList<>();
                for( DataSnapshot ds: dataSnapshot.getChildren()){
                    friends.add(getBill(ds.getKey()));
                }
                context.updateBillUsersList(friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error in getBillList");
            }
        });
    }
}
