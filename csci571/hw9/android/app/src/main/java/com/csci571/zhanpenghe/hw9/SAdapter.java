package com.csci571.zhanpenghe.hw9;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by adamhzp on 11/29/17.
 */

public class SAdapter extends ArrayAdapter<String> {

    ArrayList<String> data;
    String current;
    Context context;

    public SAdapter(Context context, int resource, ArrayList<String> data, String current){
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.current = current;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position == 0) return false;
        return !data.get(position).equalsIgnoreCase(current);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, android.view.ViewGroup parent){
        View view = super.getDropDownView(position, convertView, parent);

        TextView tv = (TextView) view;
        tv.setText(data.get(position));

        boolean disabled = !isEnabled(position);
        if(disabled){
            tv.setTextColor(Color.GRAY);
        } else{
            tv.setTextColor(Color.BLACK);
        }

        return view;
    }

    public void setCurrent(String str){
        this.current = str;
        notifyDataSetChanged();
    }
}
