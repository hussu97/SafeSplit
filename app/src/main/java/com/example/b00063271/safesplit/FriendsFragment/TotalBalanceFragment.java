package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
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

import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TotalBalanceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = "TotalBalanceFrag";

    private final String TRANSACTION_COLLECTION = "transaction";
    private final String USERS_COLLECTION = "users";

    private final int OWED = 0;
    private final int OWE = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(TRANSACTION_COLLECTION);
    private CollectionReference rf_u = db.collection(USERS_COLLECTION);

    private String userMobile;

    private OnFragmentInteractionListener mListener;

    private TabItem totalBalTabItem;
    private ListView totalBalListView;
    private TextView totalBalAmtTextView;
    private TextView totalBalPersonTextView;
    private ImageButton totalBalSettleUpButton;
    private HashMap<String,Double> balTransactions;
    private HashMap<String,Double> oweTransactions;
    private HashMap<String,Double> owedTransactions;
    private ArrayList<HashMap<String,String>> data;

    private boolean OWE_FLAG;
    private boolean OWED_FLAG;

    public TotalBalanceFragment() {
        // Required empty public constructor
    }

    public static TotalBalanceFragment newInstance(String param1) {
        TotalBalanceFragment fragment = new TotalBalanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balTransactions = new HashMap<>();
        oweTransactions = new HashMap<>();
        owedTransactions = new HashMap<>();
        OWE_FLAG=false;
        OWED_FLAG=false;
        data = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getBalTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_money_owe, container, false);
        totalBalTabItem = (TabItem) view.findViewById(R.id.owe_tab_item);
        totalBalListView = (ListView) view.findViewById(R.id.money_owe_listview);
        totalBalPersonTextView = (TextView) view.findViewById(R.id.moneyOwePerson);
        totalBalAmtTextView = (TextView) view.findViewById(R.id.moneyOweAmt);
        totalBalSettleUpButton = (ImageButton) view.findViewById(R.id.moneyOweSettleUp);
        return view;

    }

    private void getBalTransactions(String userMobile){
        getOweTransactions(userMobile);
        getOwedTransactions(userMobile);
    }

    private void getBalTransactionDetailsAux(){
        balTransactions.clear();
        data.clear();
        for (Map.Entry<String, Double> entry : oweTransactions.entrySet()){
            double prev_amount = balTransactions.containsKey(entry.getKey()) ? balTransactions.get(entry.getKey()) : 0;
            balTransactions.put(entry.getKey(),prev_amount-entry.getValue());
        }
        for (Map.Entry<String, Double> entry : owedTransactions.entrySet()){
            double prev_amount = balTransactions.containsKey(entry.getKey()) ? balTransactions.get(entry.getKey()) : 0;
            balTransactions.put(entry.getKey(),entry.getValue()-prev_amount);
        }
        getBalTransactionDetails();
    }

    private void getBalTransactionDetails(){
        for (Map.Entry<String, Double> entry : balTransactions.entrySet())
        {
            final String balID = entry.getKey();
            final double amount = entry.getValue();
            Log.d(TAG, "getBalTransactionDetails: "+balID);
            Log.d(TAG, "getBalTransactionDetails: "+amount);
            rf_u.document(balID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "onComplete: ");
                    HashMap<String,String> map=new HashMap<>();
                    map.put("amount",String.valueOf(amount));
                    if(task.isSuccessful()){
                        User user = task.getResult().toObject(User.class);
                        if(user!=null){
                            Log.d(TAG, "onComplete: User details"+user.getName());
                            map.put("person",user.getName());
                        } else{
                            map.put("person",balID);
                        }
                    } else {
                        map.put("person",balID);
                    }
                    Log.d(TAG, "onComplete: "+map.toString());
                    data.add(map);
                    if(data.size()==balTransactions.size()) updateList();
                }
            });
        }
    }

    private void getOweTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("from",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        owedTransactions.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = doc.getDouble("amount");
                            String fromID = doc.getString("to");
                            double prev_amount = oweTransactions.containsKey(fromID) ? oweTransactions.get(fromID) : 0;
                            oweTransactions.put(fromID, prev_amount + amount);
                        }
                        getBalTransactionDetailsAux();
                    }
                });
    }

    private void getOwedTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("to",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                owedTransactions.clear();
                Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    double amount = doc.getDouble("amount");
                    String fromID = doc.getString("from");
                    double prev_amount = owedTransactions.containsKey(fromID) ? owedTransactions.get(fromID) : 0;
                    owedTransactions.put(fromID, prev_amount + amount);
                }
                getBalTransactionDetailsAux();
            }});
    }

    private void updateList(){
        Log.d(TAG, "updateList: " + data.size());
        int resource = R.layout.total_bal_list;
        String[] from = {"person", "amount"};
        int[] to = {R.id.totalBalPerson, R.id.totalBalAmt};
        // create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        totalBalListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
