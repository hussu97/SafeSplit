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

import com.example.b00063271.safesplit.Entities.Transactions;
import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class MoneyOweFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = "MoneyOweFrag";

    private final String TRANSACTION_COLLECTION = "transaction";
    private final String USERS_COLLECTION = "users";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(TRANSACTION_COLLECTION);
    private CollectionReference rf_u = db.collection(USERS_COLLECTION);

    private String userMobile;

    private OnFragmentInteractionListener mListener;

    private TabItem moneyOweTabItem;
    private ListView moneyOweListView;
    private TextView moneyOweAmtTextView;
    private TextView moneyOwePersonTextView;
    private ImageButton moneyOweSettleUpButton;
    private SimpleAdapter simpleAdapter;
    private HashMap<String,Double> oweTransactions;
    private ArrayList<HashMap<String,String>> data;

    public MoneyOweFragment() {
        // Required empty public constructor
    }

    public static MoneyOweFragment newInstance(String param1) {
        MoneyOweFragment fragment = new MoneyOweFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oweTransactions = new HashMap<>();
        data = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getOweTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_money_owe, container, false);
        moneyOweTabItem = (TabItem) view.findViewById(R.id.owe_tab_item);
        moneyOweListView = (ListView) view.findViewById(R.id.money_owe_listview);
        moneyOwePersonTextView = (TextView) view.findViewById(R.id.moneyOwePerson);
        moneyOwePersonTextView = (TextView) view.findViewById(R.id.moneyOweAmt);
        moneyOweSettleUpButton = (ImageButton) view.findViewById(R.id.moneyOweSettleUp);
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
                        data.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            HashMap<String,String> map=new HashMap<>();
                            double amount = doc.getDouble("amount");
                            String fromID = doc.getString("fromID");
                            String from = doc.getString("from");
                            double prev_amount = oweTransactions.containsKey(fromID) ? oweTransactions.get(fromID) : 0;
                            oweTransactions.put(fromID, prev_amount + amount);
                            map.put("from",from);
                            map.put("fromID",fromID);
                            map.put("amount",String.valueOf(prev_amount+amount));
                            data.add(map);
                        }
                        updateList();
                    }
                });
    }

//    private void getOweTransactionDetails(){
//        for (Map.Entry<String, Double> entry : oweTransactions.entrySet())
//        {
//            final String toID = entry.getKey();
//            final double amount = entry.getValue();
//            Log.d(TAG, "getOweTransactionDetails: "+toID);
//            Log.d(TAG, "getOweTransactionDetails: "+amount);
//            rf_u.document(toID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    Log.d(TAG, "onComplete: ");
//                    HashMap<String,String> map=new HashMap<>();
//                    map.put("amount",String.valueOf(amount));
//                    if(task.isSuccessful()){
//                        User user = task.getResult().toObject(User.class);
//                        if(user!=null){
//                            Log.d(TAG, "onComplete: User details"+user.getName());
//                            map.put("person",user.getName());
//                        } else{
//                            map.put("person",toID);
//                        }
//                    } else {
//                        map.put("person",toID);
//                    }
//                    Log.d(TAG, "onComplete: "+map.toString());
//                    data.add(map);
//                    if(data.size()==oweTransactions.size())updateList();
//                }
//            });
//        }
//    }

    private void updateList(){
        Log.d(TAG, "updateList: "+data.size());
        int resource = R.layout.money_owed_list;
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
                        builder.setTitle("Settle Up")
                                .setMessage("Are you sure you want to create a transaction to receive "+amt+" from "+from)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        createTransaction(from,fromID,userMobile,userMobile,Double.valueOf(amt));
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

    private void createTransaction(String from, String fromID, String to, String toID, double amount){
        final DocumentReference df = rf_t.document();
        Transactions transaction = new Transactions(from,fromID,to,toID,amount);
        df.set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rf_u.document(userMobile).update("transactionIds", FieldValue.arrayUnion(df.getId()));
            }
        });
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
