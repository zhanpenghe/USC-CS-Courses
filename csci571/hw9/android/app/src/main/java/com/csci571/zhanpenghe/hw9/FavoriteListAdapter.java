package com.csci571.zhanpenghe.hw9;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by adamhzp on 11/28/17.
 */

public class FavoriteListAdapter extends BaseAdapter {

    private List<JSONObject> data;
    private List<JSONObject> sortedData;
    private List<Boolean> done;

    private Context context;
    private Activity activity;
    private String sortBy;
    private String order;

    public FavoriteListAdapter(Context context, Activity activity){
        super();
        this.context = context;
        this.activity = activity;
        sortBy = "Sort by";
        order = "Ascending";
        data = new ArrayList<>();

        // Load data from shared preference
        SharedPreferences pref = context.getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
        if(pref!=null){
            String favList = pref.getString("all_fav_stocks",  null);
            if(favList == null) return;
            try{
                JSONArray array = new JSONArray(favList);
                for(int i = 0; i<array.length(); i++){
                    try{
                        JSONObject obj = new JSONObject(array.getString(i));
                        data.add(obj);
                    }catch (Exception ex){
                        continue;
                    }
                }
                sortedData = new ArrayList<>(data);
            }catch (Exception e){

            }
        }
    }

    public void done(int i, JSONObject obj, String symbol){
        try{
            done.set(i, true);
            boolean reallyDone = true;
            for(Boolean d: done){
                reallyDone = (d && reallyDone);
            }
            updateValue(obj, reallyDone, symbol);
        }catch (Exception e){

        }
    }

    public List<JSONObject> getData(){
        done = new ArrayList<>();
        for(int i=0; i<data.size();i++){
            done.add(false);
        }
        return this.data;
    }

    public void reloadData(){
        SharedPreferences pref = context.getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
        data = new ArrayList<>();
        if(pref!=null){
            String favList = pref.getString("all_fav_stocks",  null);
            if(favList == null) return;
            try{
                JSONArray array = new JSONArray(favList);
                for(int i = 0; i<array.length(); i++){
                    try{
                        JSONObject obj = new JSONObject(array.getString(i));
                        data.add(obj);
                    }catch (Exception ex){
                        continue;
                    }
                }

                reorder(this.sortBy, this.order);
            }catch (Exception e){

            }
        }
        notifyDataSetChanged();
    }

    public void reorder(String sortBy, String order){
        this.sortBy = sortBy;
        this.order = order;
        sortedData = new ArrayList<>(data);
        if(sortBy.equalsIgnoreCase("Default")|| sortBy.equalsIgnoreCase("Sort by")){
            if(order.equalsIgnoreCase("Ascending")||order.equalsIgnoreCase("Order")) this.sortedData = new ArrayList<>(data);
            else{
                this.sortedData = new ArrayList<>(data);
                Collections.reverse(this.sortedData);
            }
        }
        else if(sortBy.equalsIgnoreCase("Price")){
            if(order.equalsIgnoreCase("ascending")||order.equalsIgnoreCase("Order")) {
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(jsonObject.getString("lastDayClose")) - Float.parseFloat(t1.getString("lastDayClose"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }else{
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(t1.getString("lastDayClose")) - Float.parseFloat(jsonObject.getString("lastDayClose"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }
        }
        else if(sortBy.equalsIgnoreCase("symbol")){
            if(order.equalsIgnoreCase("ascending") || order.equalsIgnoreCase("Order")) {
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return jsonObject.getString("symbol").compareTo(t1.getString("symbol"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }else{
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return -1*jsonObject.getString("symbol").compareTo(t1.getString("symbol"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }
        }
        else if(sortBy.equalsIgnoreCase("change")){
            if(order.equalsIgnoreCase("ascending")||order.equalsIgnoreCase("Order")) {
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(jsonObject.getString("change")) - Float.parseFloat(t1.getString("change"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }else{
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(t1.getString("change")) - Float.parseFloat(jsonObject.getString("change"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }
        }
        else if(sortBy.equalsIgnoreCase("change (%)")){
            if(order.equalsIgnoreCase("ascending")||order.equalsIgnoreCase("Order")) {
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(jsonObject.getString("changePercent")) - Float.parseFloat(t1.getString("changePercent"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }else{
                Collections.sort(sortedData, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObject, JSONObject t1) {
                        try {
                            return Float.parseFloat(t1.getString("changePercent")) - Float.parseFloat(jsonObject.getString("changePercent"))>0?1:-1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        try {
            return this.data.get(i);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View resultView = inflater.inflate(R.layout.fav_list_component, parent, false);

        TextView symbol = resultView.findViewById(R.id.fav_symbol);
        TextView price = resultView.findViewById(R.id.fav_price);
        TextView change = resultView.findViewById(R.id.fav_change);

        try{
            symbol.setText(sortedData.get(i).getString("symbol"));
            price.setText(String.format("%.2f", sortedData.get(i).getDouble("lastDayClose")));
            String changeStr = String.format("%.2f", sortedData.get(i).getDouble("change"));
            String changePercent = changeStr+" ("+String.format("%.2f", sortedData.get(i).getDouble("changePercent"))+"%)";
            change.setText(changePercent);
            if(sortedData.get(i).getDouble("change")>0){
                change.setTextColor(Color.GREEN);
            }else{
                change.setTextColor(Color.RED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        resultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(activity, DetailsActivity.class);
                    intent.putExtra("symbol", sortedData.get(view.getId()).getString("symbol"));
                    activity.startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        resultView.setLongClickable(true);
        resultView.setId(i);
        activity.registerForContextMenu(resultView);

        return resultView;
    }

    public void remove(int i){
        try{
            String symbol = sortedData.get(i).getString("symbol");
            JSONArray array = new JSONArray();
            for(JSONObject obj: data){
                try{
                    if(obj.getString("symbol").equalsIgnoreCase(symbol)) continue;
                    array.put(obj);
                }catch (Exception ex){
                    ex.printStackTrace();
                    continue;
                }
            }
            SharedPreferences pref = context.getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("all_fav_stocks", array.toString());
            editor.apply();
            reloadData();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void updateValue(JSONObject obj, boolean done, String symbol){
        try{
            for(JSONObject o: data){
                if(symbol.equalsIgnoreCase(o.getString("symbol"))) {
                    o.put("change", obj.getDouble("change"));
                    o.put("changePercent", obj.getDouble("changePercent"));
                    o.put("lastDayClose", obj.getDouble("lastDayClose"));
                    o.put("lastDayVol", obj.getDouble("lastDayVol"));
                }
            }
            if(done){
                JSONArray array = new JSONArray();
                for(JSONObject o: data){
                    try{
                        array.put(o);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        continue;
                    }
                }
                SharedPreferences pref = context.getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("all_fav_stocks", array.toString());
                editor.apply();
                reloadData();
                activity.findViewById(R.id.favoriteProgress).setVisibility(View.INVISIBLE);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(obj);
            Toast toast = Toast.makeText(this.context, "Failed to update "+symbol+" data.", Toast.LENGTH_SHORT);
            toast.show();
            if(done){
                JSONArray array = new JSONArray();
                for(JSONObject o: data){
                    try{
                        array.put(o);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        continue;
                    }
                }
                SharedPreferences pref = context.getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("all_fav_stocks", array.toString());
                editor.apply();
                reloadData();
                activity.findViewById(R.id.favoriteProgress).setVisibility(View.INVISIBLE);
            }
        }
    }


}