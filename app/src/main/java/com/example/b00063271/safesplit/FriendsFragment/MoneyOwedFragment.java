package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MoneyOwedFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = "MoneyOwedFrag";

    private final String TRANSACTION_COLLECTION = "transaction";
    private final String USERS_COLLECTION = "users";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(TRANSACTION_COLLECTION);
    private CollectionReference rf_u = db.collection(USERS_COLLECTION);

    private String userMobile;

    private OnFragmentInteractionListener mListener;

    private TabItem moneyOwedTabItem;
    private ListView moneyOwedListView;
    private TextView moneyOwedAmtTextView;
    private TextView moneyOwedPersonTextView;
    private ImageButton moneyOwedSettleUpButton;
    private HashMap<String,Double> owedTransactions;
    private ArrayList<HashMap<String,String>>data;

    public MoneyOwedFragment() {
        // Required empty public constructor
    }

    public static MoneyOwedFragment newInstance(String param1) {
        MoneyOwedFragment fragment = new MoneyOwedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        owedTransactions = new HashMap<>();
        data = new ArrayList<>();
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getOwedTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_money_owed, container, false);
        moneyOwedTabItem = (TabItem) view.findViewById(R.id.owed_tab_item);
        moneyOwedListView = (ListView) view.findViewById(R.id.money_owed_listview);
        moneyOwedPersonTextView = (TextView) view.findViewById(R.id.moneyOwedPerson);
        moneyOwedPersonTextView = (TextView) view.findViewById(R.id.moneyOwedAmt);
        moneyOwedSettleUpButton = (ImageButton) view.findViewById(R.id.moneyOwedSettleUp);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getOwedTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("to",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        owedTransactions.clear();
                        data.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = doc.getDouble("amount");
                            String fromID = doc.getString("from");
                            double prev_amount = owedTransactions.containsKey(fromID) ? owedTransactions.get(fromID) : 0;
                            owedTransactions.put(fromID, prev_amount + amount);
                        }
                        getOwedTransactionDetails();
                    }
                });
    }

    private void getOwedTransactionDetails(){
        for (Map.Entry<String, Double> entry : owedTransactions.entrySet())
        {
            final String fromID = entry.getKey();
            final double amount = entry.getValue();
            Log.d(TAG, "getOwedTransactionDetails: "+fromID);
            Log.d(TAG, "getOwedTransactionDetails: "+amount);
            rf_u.document(fromID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                            map.put("person",fromID);
                        }
                    } else {
                        map.put("person",fromID);
                    }
                    Log.d(TAG, "onComplete: "+map.toString());
                    data.add(map);
                    if(data.size()==owedTransactions.size())updateList();
                }
            });
        }
        Log.d(TAG, "getOwedTransactionDetails: updateList");
        updateList();
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
    private void updateList(){
        Log.d(TAG, "updateList: "+data.size());
        int resource = R.layout.money_owed_list;
        String[] from = {"person", "amount"};
        int[] to = {R.id.moneyOwedPerson, R.id.moneyOwedAmt};
        // create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        moneyOwedListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
