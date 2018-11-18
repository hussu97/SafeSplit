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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.C;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.b00063271.safesplit.AddBill.current_amount;
import static com.example.b00063271.safesplit.AddBill.splittersexact;
import static com.example.b00063271.safesplit.AddBill.splitterspercent;
import static com.example.b00063271.safesplit.AddBill.users;
import static com.example.b00063271.safesplit.SplitActivity.percenttotal;
import static com.example.b00063271.safesplit.AddBill.users_without_custom;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link splitpercent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link splitpercent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class splitpercent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public splitpercent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment splitpercent.
     */
    // TODO: Rename and change types and number of parameters
    public static splitpercent newInstance(String param1, String param2) {
        splitpercent fragment = new splitpercent();
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

    static ListView percentpayers;
    private Float amount_sum_value = 0f;
    private Float amount_sum_tv = 0f;
    private ArrayList<Float> each_percent_value;
    private ArrayList<Float> each_percent_tv;
    private TextView infopercent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_splitpercent, container, false);

        infopercent = view.findViewById(R.id.infopercent);
        String startStr = "Percent remaining: ";
        String mid = "100";
        String end = "%";
        String finalStr = startStr+mid+end;
        Spannable spannable = new SpannableString(finalStr);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), startStr.length(), (startStr + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        infopercent.setText(spannable, TextView.BufferType.SPANNABLE);

        each_percent_value = new ArrayList<>();
        for(int i = 0; i < users_without_custom.size(); i++){
            each_percent_value.add(0f);
        }
        each_percent_tv = new ArrayList<>();
        for(int i = 0; i < users_without_custom.size(); i++){
            each_percent_tv.add(0f);
        }

        for(int i = 0; i < splitterspercent.size(); i++){
            splitterspercent.get(i).put("amount", Float.toString(0f));
        }


        //List View
        //------------------------------------------------------------------------------------------
        percentpayers = (ListView) view.findViewById(R.id.percentpayerslist);

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (String user:users){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", user);
            data.add(map);
        }



        int resource = R.layout.percentpayerslist_item;
        String[] from = {"name"};
        int[] to = {R.id.percentpayerslist_item};

        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                EditText percent = (EditText) v.findViewById(R.id.percentamount);

                if(position == 0) v.setVisibility(View.INVISIBLE);
                percent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().equals(".")) {
                            splitterspercent.get(position - 1).put("amount", Float.toString(0f));
                            each_percent_value.set(position - 1, 0f);
                            each_percent_tv.set(position - 1, 0f);
                        }
                        else if(!s.toString().isEmpty()){
                            Float _percent_ = Float.parseFloat(s.toString());
                            Float _amount_ = (_percent_*current_amount)/100;
                            splitterspercent.get(position - 1).put("amount", Float.toString(_amount_));
                            each_percent_value.set(position - 1, _amount_);
                            each_percent_tv.set(position - 1, _percent_);
                        }
                        else {
                            splitterspercent.get(position - 1).put("amount", Float.toString(0f));
                            each_percent_value.set(position - 1, 0f);
                            each_percent_tv.set(position - 1, 0f);
                        }
                        amount_sum_value = 0f;
                        for(Float am:each_percent_value)
                            amount_sum_value+=am;
                        amount_sum_tv = 0f;
                        for(Float am:each_percent_tv)
                            amount_sum_tv+=am;
                        percenttotal = 100 - amount_sum_tv;
//                        Float percentpayed = 100*((Float)amount_sum/(Float)current_amount);
                        String startStr = "Percent remaining: ";
                        String mid = Double.toString(C.round(100 - amount_sum_tv));
                        String end = "%";
                        String finalStr = startStr+mid+end;
                        Spannable spannable = new SpannableString(finalStr);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), startStr.length(), (startStr + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        infopercent.setText(spannable, TextView.BufferType.SPANNABLE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        System.out.println("helellloeleoleol");
                    }
                });
                return v;
            }
        };
        percentpayers.setAdapter(adapter);
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
        for(int i = 0; i < each_percent_value.size(); i++){
            splitterspercent.get(i).put("amount", Float.toString(each_percent_value.get(i)));
        }
        for(int i = 0; i < splitterspercent.size(); i++){
            System.out.println(splitterspercent.get(i).get("name") + " " + splitterspercent.get(i).get("amount"));
        }
    }

}
