package com.example.b00063271.safesplit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.C;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.b00063271.safesplit.AddBill.current_amount;
import static com.example.b00063271.safesplit.AddBill.splittersequal;
import static com.example.b00063271.safesplit.AddBill.splittersexact;
import static com.example.b00063271.safesplit.AddBill.splitterspercent;
import static com.example.b00063271.safesplit.SplitActivity.exacttotal;
import static com.example.b00063271.safesplit.AddBill.users;
import static com.example.b00063271.safesplit.AddBill.users_without_custom;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link splitexactamounts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link splitexactamounts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class splitexactamounts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public splitexactamounts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment splitexactamounts.
     */
    // TODO: Rename and change types and number of parameters
    public static splitexactamounts newInstance(String param1, String param2) {
        splitexactamounts fragment = new splitexactamounts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    static ListView exactpayers;
    private Float amount_sum = 0f;
    private ArrayList<Float> each_amount;
    private TextView infoexact;


    private ArrayList<EditText> edittexts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splitexactamounts, container, false);
        infoexact = view.findViewById(R.id.infoexact);
        String start = "Amount remaining: ";
        String mid = Double.toString(C.round(current_amount - amount_sum));
        String end = "AED";
        String finalStr = start+mid+end;
        Spannable spannable = new SpannableString(finalStr);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), start.length(), (start + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        infoexact.setText(spannable, TextView.BufferType.SPANNABLE);

        each_amount = new ArrayList<>();
        for(int i = 0; i < users_without_custom.size(); i++){
            each_amount.add(0f);
        }

        for(int i = 0; i < splittersexact.size(); i++){
            splittersexact.get(i).put("amount", Float.toString(0f));
        }


        //List View
        //------------------------------------------------------------------------------------------
        exactpayers = (ListView) view.findViewById(R.id.exactpayerslist);

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (String user:users){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", user);
            data.add(map);
        }


        int resource = R.layout.exactpayerslist_item;
        String[] from = {"name"};
        int[] to = {R.id.exactpayerslist_item};

        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                final EditText exactamount = (EditText) v.findViewById(R.id.exactamount);

                if(position == 0) v.setVisibility(View.INVISIBLE);
                exactamount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        System.out.println("---------------------------------------");
                        if (exactamount.getText().toString().equals(".")) {
                            System.out.println("111111111---------------------------------------");
                            splittersexact.get(position - 1).put("amount", Float.toString(0f));
                            each_amount.set(position - 1, 0f);
                        }
                        else if(!exactamount.getText().toString().isEmpty()){
                            System.out.println("2222222222222---------------------------------------");
                            splittersexact.get(position - 1).put("amount", exactamount.getText().toString());
                            each_amount.set(position - 1, Float.parseFloat(exactamount.getText().toString()));
                        }
                        else {
                            System.out.println("3333333333333---------------------------------------");
                            splittersexact.get(position - 1).put("amount", Float.toString(0f));
                            each_amount.set(position - 1, 0f);
                        }
                        amount_sum = 0f;

                        for(Float am:each_amount)
                            amount_sum+=am;

                        String start = "Amount remaining: ";
                        exacttotal = current_amount - amount_sum;
                        String mid = Double.toString(C.round(current_amount - amount_sum));
                        String end = "AED";
                        String finalStr = start+mid+end;
                        Spannable spannable = new SpannableString(finalStr);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), start.length(), (start + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        infoexact.setText(spannable, TextView.BufferType.SPANNABLE);
                    }
                });
                return v;
            }
        };
        exactpayers.setAdapter(adapter);
        //equalpayers.setOnItemClickListener(this);
        //------------------------------------------------------------------------------------------
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void submit(){
        System.out.println("+++++++++++++++++++++++");
        for(int i = 0; i < each_amount.size(); i++){
            splittersexact.get(i).put("amount", Float.toString(each_amount.get(i)));
        }
        for(int i = 0; i < splittersexact.size(); i++){
            System.out.println(splittersexact.get(i).get("name") + " " + splittersexact.get(i).get("amount"));
        }
    }

}
