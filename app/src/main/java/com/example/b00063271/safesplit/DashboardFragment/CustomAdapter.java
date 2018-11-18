package com.example.b00063271.safesplit.DashboardFragment;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b00063271.safesplit.Entities.Activities;
import com.example.b00063271.safesplit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends BaseAdapter{
    ArrayList<Activities> result;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomAdapter(DashboardFragment mainActivity, ArrayList<Activities> activities) {
        result = activities;
        context=mainActivity.getContext();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { return result.size(); }
    @Override
    public Object getItem(int position) { return position; }
    @Override
    public long getItemId(int position) { return position; }
    public class Holder {
        TextView tv;
        TextView ts;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.dashboard_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.dashboardItemTextView);
        holder.img=(ImageView) rowView.findViewById(R.id.dashboardItemImage);
        holder.ts=(TextView) rowView.findViewById(R.id.dashboardTimeStampTextView);
        String activityStart = result.get(position).getActivityString().split("-")[0];
        String activityMiddle = result.get(position).getActivityString().split("-")[1];
        String activityEnd = result.get(position).getActivityString().split("-")[2];
        String descriptionFinal = activityStart + activityMiddle + activityEnd;
        Spannable spannable = new SpannableString(descriptionFinal);
        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), activityStart.length(), (activityStart + activityMiddle).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv.setText(spannable, TextView.BufferType.SPANNABLE);
        Date date = result.get(position).getTimeStamp();
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate = simpleDateFormat.format(date);
        holder.ts.setText(newDate);
        holder.img.setImageResource((int)result.get(position).getActivityType());
        return rowView;
    }

}