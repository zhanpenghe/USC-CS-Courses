package com.csci571.zhanpenghe.hw9;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamhzp on 11/24/17.
 */


public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> data;

    public AutoCompleteAdapter(Context context, int resource){
        super(context, resource);
        this.data = new ArrayList<>();
    }

    @Override
    public int getCount(){
        return this.data.size();
    }

    @Override
    public String getItem (int position)
    {
        return data.get(position);
    }

    public void setData(List<String> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                results.values = data;
                results.count = data.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null) notifyDataSetChanged();
                else notifyDataSetInvalidated();
            }
        };
    }
}
