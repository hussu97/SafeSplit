package com.example.b00063271.safesplit.Database;

import com.example.b00063271.safesplit.Entities.Bill;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BillDB {
    private static final String DB_NAME = "bills";

    private static FirebaseDatabase db;
    private static DatabaseReference rf;
    public static void initializeBillDB(){
        db = FirebaseDatabase.getInstance();
        rf = db.getReference(DB_NAME);
    }
    public void addBill(Bill bill){
        String key = rf.push().getKey();
        rf.child(key).setValue(bill);
    }
    public void deleteBill(Bill bill){
        rf.child(bill.getId()).removeValue();
    }

}
