package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.TransactionDB;
import com.example.b00063271.safesplit.Entities.Transactions;
import com.example.b00063271.safesplit.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class MoneyOweFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "MoneyOweFrag";

    private ActivityDB activityDB;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(C.COLLECTION_TRANSACTION);

    private String userMobile;
    private String userName;

    private TransactionDB transactionDB;
    private MoneyOweFragment.OnFragmentInteractionListener mListener;
    private TransactionDB.OnDatabaseInteractionListener mDBListener= new TransactionDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, ArrayList<Double> a, ArrayList<Double> b, ArrayList<Double> c) { }
    };

    private ListView moneyOweListView;
    private SimpleAdapter simpleAdapter;
    private HashMap<String,Double> oweTransactions;
    private HashMap<String,String> oweTransactionsNames;
    private HashMap<String,ArrayList<String>> oweTransactionsIDs;
    private ArrayList<HashMap<String,String>> data;

    public MoneyOweFragment() {
        // Required empty public constructor
    }

    public static MoneyOweFragment newInstance(String param1, String param2) {
        MoneyOweFragment fragment = new MoneyOweFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Called");
        activityDB = new ActivityDB();
        oweTransactions = new HashMap<>();
        oweTransactionsNames = new HashMap<>();
        oweTransactions = new HashMap<>();
        oweTransactionsIDs = new HashMap<>();
        transactionDB = new TransactionDB(mDBListener);
        data = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Called");
        // Inflate the layout for this fragment
        getOweTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_money_owe, container, false);
        moneyOweListView = (ListView) view.findViewById(R.id.money_owe_listview);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getOweTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("toID",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        oweTransactions.clear();
                        oweTransactionsNames.clear();
                        oweTransactionsIDs.clear();
                        data.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            if(doc.getString(C.TRANSACTION_GROUP_ID)!=null)continue;
                            double amount = C.round(doc.getDouble("amount"));
                            String fromID = doc.getString("fromID");
                            String from = doc.getString("from");
                            double prev_amount = oweTransactions.containsKey(fromID) ? oweTransactions.get(fromID) : 0;
                            oweTransactions.put(fromID, C.round(prev_amount + amount));
                            oweTransactionsNames.put(fromID,from);
                            ArrayList<String> prev_transactions = oweTransactionsIDs.containsKey(fromID) ? oweTransactionsIDs.get(fromID) : new ArrayList<String>();
                            prev_transactions.add(doc.getId());
                            oweTransactionsIDs.put(fromID,prev_transactions);
                        }
                        updateList();
                    }
                });
    }
    
    private void updateList(){
        for(Map.Entry<String, Double> entry : oweTransactions.entrySet()){
            HashMap<String,String> map = new HashMap<>();
            map.put("from",oweTransactionsNames.get(entry.getKey()));
            map.put("fromID",entry.getKey());
            map.put("amount",String.valueOf(entry.getValue()));
            data.add(map);
        }
        try {
            TextView empty = super.getView().findViewById(R.id.noMoneyOweTextView);
            if (data.size() == 0) {
                empty.setVisibility(View.VISIBLE);
                return;
            } else empty.setVisibility(View.GONE);
        } catch(NullPointerException e) { }
        int resource = R.layout.money_owe_list;
        String[] from = {"from","fromID", "amount"};
        int[] to = {R.id.moneyOwePerson,R.id.moneyOwePersonID, R.id.moneyOweAmt};
        // create and set the adapter
        simpleAdapter=new SimpleAdapter(getActivity(),data,resource,from,to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ImageButton b = (ImageButton) v.findViewById(R.id.moneyOweSettleUp);
                final String from = ((TextView) v.findViewById(R.id.moneyOwePerson)).getText().toString();
                final String amt = ((TextView) v.findViewById(R.id.moneyOweAmt)).getText().toString();
                final String fromID = ((TextView) v.findViewById(R.id.moneyOwePersonID)).getText().toString();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle(C.SETTLE_UP)
                                .setMessage("Are you sure you want to create a transaction to send "+amt+" to "+from)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(String transactionID: oweTransactionsIDs.get(fromID)){ transactionDB.deleteTransaction(userMobile,transactionID); }
                                        activityDB.createActivity(userMobile,"You settled your debt with "+from+" by paying -"+amt+"- AED",C.ACTIVITY_TYPE_SETTLE_UP, new Date());
                                        activityDB.createActivity(fromID,"Your debt with "+userName+" has been settled by receiving -"+amt+"- AED",C.ACTIVITY_TYPE_SETTLE_UP, new Date());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                return v;
            }
        };
        moneyOweListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
