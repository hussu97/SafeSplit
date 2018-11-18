package com.example.b00063271.safesplit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.Database.C;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.b00063271.safesplit.AddBill.current_amount;
import static com.example.b00063271.safesplit.AddBill.payers;
import static com.example.b00063271.safesplit.AddBill.split;
import static com.example.b00063271.safesplit.AddBill.splittersequal;
import static com.example.b00063271.safesplit.AddBill.users;
import static com.example.b00063271.safesplit.AddBill.users_without_custom;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link splitequally.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link splitequally#newInstance} factory method to
 * create an instance of this fragment.
 */
public class splitequally extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public splitequally() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment splitequally.
     */
    // TODO: Rename and change types and number of parameters
    public static splitequally newInstance(String param1, String param2) {
        splitequally fragment = new splitequally();
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


    private ListView equalpayers;
    private TextView info;
    private float amount_per_person;
    private int no_of_users;
    //private CheckBox cb_temp;
    private SimpleAdapter adapter;
    static int prevpos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spliequally, container, false);

        System.out.println(users.size());
        for(int i = 0; i < users.size(); i++) System.out.println(users.get(i));
        System.out.println(users_without_custom.size());
        for(int i = 0; i < users_without_custom.size(); i++) System.out.println(users_without_custom.get(i));


        //List View
        //------------------------------------------------------------------------------------------
        equalpayers = (ListView) view.findViewById(R.id.equalpayerslist);
        info = (TextView) view.findViewById(R.id.infoequal);

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (String user:users_without_custom){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", user);
            data.add(map);
        }


        int resource = R.layout.equalpayerslist_item;
        String[] from = {"name"};
        int[] to = {R.id.equalpayerslist_item};

        adapter = new SimpleAdapter(getActivity(),data,resource,from,to){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                final CheckBox cb_temp = (CheckBox) v.findViewById(R.id.splittingcb);

                final HashMap<String, String> splitter = new HashMap<>();
                final int pos = position;
                cb_temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cb_temp.isChecked()) {
                            no_of_users++;

                            amount_per_person = (Float)current_amount/no_of_users;
                            splitter.put("name", users_without_custom.get(pos));
                            splitter.put("amount", Float.toString(amount_per_person));
                            splittersequal.set(pos, splitter);
                            for(int i = 0; i < splittersequal.size(); i++){
                                if(!splittersequal.get(i).get("amount").equals("0")){
                                    splittersequal.get(i).put("amount", Float.toString(amount_per_person));
                                }
                            }
                        }
                        else {
                            no_of_users--;
                            amount_per_person = (Float)current_amount/no_of_users;


                            for(int i = 0; i < splittersequal.size(); i++){
                                if (splittersequal.get(i).get("name").equals(users_without_custom.get(pos)) || splittersequal.get(i).get("amount").equals("0")){
                                    splittersequal.get(i).put("amount", "0");
                                }
                                else {
                                    splittersequal.get(i).put("amount", Float.toString(amount_per_person));
                                }
                            }
                            prevpos = pos;
                        }
                        String start = "AED ";
                        String mid = Double.toString(C.round(amount_per_person));
                        String end = "/person";
                        String finalStr = start+mid+end;
                        Spannable spannable = new SpannableString(finalStr);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), start.length(), (start + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        info.setText(spannable, TextView.BufferType.SPANNABLE);
                    }
                });



                return v;
            }

        };
        equalpayers.setAdapter(adapter);
        equalpayers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        equalpayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                if (lv.isItemChecked(position)){

                }
                else{

                }
            }
        });
        //------------------------------------------------------------------------------------------

        no_of_users = users_without_custom.size();


        amount_per_person = (Float)current_amount/no_of_users;
        String start = "AED ";
        String mid = Double.toString(C.round(amount_per_person));
        String end = "/person";
        String finalStr = start+mid+end;
        Spannable spannable = new SpannableString(finalStr);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondary)), start.length(), (start + mid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        info.setText(spannable, TextView.BufferType.SPANNABLE);
        for(int i = 0; i < users_without_custom.size(); i++){
            splittersequal.get(i).put("amount", Float.toString(amount_per_person));
        }


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
        for(int i = 0; i < splittersequal.size(); i++){
            System.out.println(splittersequal.get(i).get("name") + " " + splittersequal.get(i).get("amount"));
        }
    }

}
