package com.csci571.zhanpenghe.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adamhzp on 11/25/17.
 */

public class Tab1Current extends Fragment{

    boolean done[] = new boolean[9];
    boolean error[] = new boolean[9];
    JSONObject data[] = new JSONObject[9];

    //View Objects
    ProgressBar tableBar;
    ProgressBar chartBar;
    TableLayout detailsTable;
    View rootView;
    WebView hiChartView;
    Spinner spinner;
    Button changeBtn;
    ImageButton favBtn;
    TextView tableError;
    TextView chartError;

    String symbol;
    int currentChart = 0;
    int selectedChart = 0;

    ShareDialog shareDialog;

    public void shareToFB(){
        if(!done[currentChart] || error[currentChart]){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getContext(), "The chart is not loaded yet. Please retry when it is successfully loaded.", duration);
            toast.show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_img", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://export.highcharts.com/"+response)).build();
                shareDialog.show(content);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("async","true");
                params.put("type", "png");
                if(currentChart==0){
                    try{
                        params.put("options", data[currentChart].getJSONObject("hi_charts_obj").toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else {
                    params.put("options", data[currentChart].toString());
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
    }

    private void setFavBtnImageSource(){
        SharedPreferences pref = getActivity().getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
        if(pref == null){
            favBtn.setImageResource(R.drawable.empty);
            return;
        }
        String fetched = pref.getString("all_fav_stocks",null);
        if(fetched == null){
            favBtn.setImageResource(R.drawable.empty);
            return;
        }else{
            try{
                JSONArray array = new JSONArray(fetched);
                boolean isFav = false;
                int i = 0;
                for(; i<array.length(); i++){
                    try{
                        JSONObject obj = new JSONObject(array.getString(i));
                        if (obj.getString("symbol").equalsIgnoreCase(symbol)){
                            isFav = true;
                            break;
                        }
                    }catch(Exception ex){
                        continue;
                    }
                }
                if(isFav){
                    favBtn.setImageResource(R.drawable.filled);
                }else{
                    favBtn.setImageResource(R.drawable.empty);
                }
            }catch (Exception e){
                favBtn.setImageResource(R.drawable.empty);
                return;
            }
        }
    }

    public void saveToFavorite(){
        if(!done[0] || error[0]){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getContext(), "Stock data is not loaded. Please retry when they are successfully loaded.", duration);
            toast.show();
            return;
        }
        //check if it is favorite already
        SharedPreferences pref = getActivity().getSharedPreferences("favorite_stocks", Context.MODE_PRIVATE);
        try{
            String fetched = pref.getString("all_fav_stocks",null);
            SharedPreferences.Editor editor = pref.edit();

            if(fetched == null){    // first time, just add info to shared preference
                JSONArray array = new JSONArray();
                String infos = data[0].getString("table_obj");
                array.put(infos);
                editor.putString("all_fav_stocks", array.toString());
                editor.apply();
                favBtn.setImageResource(R.drawable.filled);
            }else{

                JSONArray array = new JSONArray(fetched);
                boolean isFav = false;
                int i = 0;
                for(; i<array.length(); i++){
                    try{
                        JSONObject obj = new JSONObject(array.getString(i));
                        if (obj.getString("symbol").equalsIgnoreCase(symbol)){
                            isFav = true;
                            break;
                        }
                    }catch(Exception ex){
                        continue;
                    }
                }

                if(isFav){
                    array.remove(i);
                    editor.putString("all_fav_stocks", array.toString());
                    editor.apply();
                    favBtn.setImageResource(R.drawable.empty);
                }else{
                    String infos = data[0].getString("table_obj");
                    array.put(infos);
                    editor.putString("all_fav_stocks", array.toString());
                    editor.apply();
                    favBtn.setImageResource(R.drawable.filled);
                }
            }
        }catch(Exception e){

        }
    }

    private int getDataId(String str){
        switch (str){
            case "Price":
                return 0;
            case "SMA":
                return 1;
            case "EMA":
                return 2;
            case "STOCH":
                return 3;
            case "RSI":
                return 4;
            case "ADX":
                return 5;
            case "CCI":
                return 6;
            case "BBANDS":
                return 7;
            case "MACD":
                return 8;
            default:
                return -1;
        }
    }

    private void setChangeButton(){
        if(selectedChart == currentChart) changeBtn.setEnabled(false);
        else changeBtn.setEnabled(true);
    }

    public void updateHicharts(boolean isFromBUtton, int dataUpdate){
        if(isFromBUtton) {
            currentChart = selectedChart;
        }
        if(dataUpdate == currentChart){
            if(done[currentChart] && !error[currentChart]){
                hiChartView.setVisibility(View.INVISIBLE);
                chartBar.setVisibility(View.INVISIBLE);
                chartError.setVisibility(View.INVISIBLE);
                hiChartView.loadUrl("file:///android_asset/html/hi_charts.html");
                hiChartView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url){
                        try {
                            if(currentChart == 0){
                                hiChartView.loadUrl("javascript:setChart(" + data[currentChart].getString("hi_charts_obj") + ")");
                            }else{
                                hiChartView.loadUrl("javascript:setChart(" + data[currentChart].toString() + ")");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                hiChartView.setVisibility(View.VISIBLE);
            }else if(!done[currentChart]){
                hiChartView.setVisibility(View.INVISIBLE);
                chartError.setVisibility(View.INVISIBLE);
                chartBar.setVisibility(View.VISIBLE);
            }else{
                chartBar.setVisibility(View.INVISIBLE);
                hiChartView.setVisibility(View.INVISIBLE);
                chartError.setVisibility(View.VISIBLE);
            }
        }
        setChangeButton();
    }

    // A method to check if data is done with transfer and update view
    public void updateView(int dataUpdate){
        try {
            //table
            setFavBtnImageSource();
            if(dataUpdate == 0){
                if (!done[0]) {
                    tableBar.setVisibility(View.VISIBLE);
                    tableError.setVisibility(View.INVISIBLE);
                    detailsTable.setVisibility(View.INVISIBLE);
                } else if(error[0]){
                    tableBar.setVisibility(View.INVISIBLE);
                    tableError.setVisibility(View.VISIBLE);
                    detailsTable.setVisibility(View.INVISIBLE);
                }else{
                    tableBar.setVisibility(View.INVISIBLE);
                    detailsTable.setVisibility(View.VISIBLE);
                    tableError.setVisibility(View.INVISIBLE);
                    updateTable(data[0]);
                }
            }
            updateHicharts(false, dataUpdate);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getContext().getApplicationContext());

        rootView = inflater.inflate(R.layout.tab1current, container, false);
        shareDialog = new ShareDialog(this);

        tableBar = rootView.findViewById(R.id.progressBar);
        tableError = rootView.findViewById(R.id.table_error_msg);
        chartBar = rootView.findViewById(R.id.chartProgressBar);
        chartError = rootView.findViewById(R.id.chart_error_message);
        detailsTable = rootView.findViewById(R.id.detailTable);

        hiChartView = rootView.findViewById(R.id.hi_charts);
        hiChartView.getSettings().setJavaScriptEnabled(true);

        spinner = rootView.findViewById(R.id.indicators_options);
        changeBtn = rootView.findViewById(R.id.changeBtn);

        favBtn = rootView.findViewById(R.id.favBtn);
        ImageButton innerBtn = rootView.findViewById(R.id.shareBtn);

        // Add listener on spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedChart = getDataId(spinner.getSelectedItem().toString());
                setChangeButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Add listener to change button
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHicharts(true, selectedChart);
            }
        });

        // Add listener to favorite button
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFavorite();
            }
        });

        innerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToFB();
            }
        });

        LoginManager.getInstance().logOut();
        updateView(0);
        return rootView;
    }

    public void updateTable(JSONObject obj){
        try{
            DecimalFormat df = new DecimalFormat("#######0.00");
            ((TextView)rootView.findViewById(R.id.symbol)).setText((String)obj.getJSONObject("table_obj").getString("symbol"));
            ((TextView)rootView.findViewById(R.id.open)).setText(df.format(obj.getJSONObject("table_obj").getDouble("lastDayOpen")));
            ((TextView)rootView.findViewById(R.id.close)).setText(df.format( obj.getJSONObject("table_obj").getDouble("lastDayClose")));

            double hi = obj.getJSONObject("table_obj").getDouble("hi");
            double lo = obj.getJSONObject("table_obj").getDouble("lo");

            ((TextView)rootView.findViewById(R.id.range)).setText(df.format(lo)+" - "+df.format(hi));
            ((TextView)rootView.findViewById(R.id.timestamp)).setText((String)obj.getJSONObject("table_obj").getString("lastDay"));
            ((TextView)rootView.findViewById(R.id.volume)).setText((String)obj.getJSONObject("table_obj").getString("lastDayVol"));
            ((TextView)rootView.findViewById(R.id.price)).setText(df.format( obj.getJSONObject("table_obj").getDouble("lastDayClose")));

            double change = obj.getJSONObject("table_obj").getDouble("change");
            double changePercent = obj.getJSONObject("table_obj").getDouble("changePercent");
            if(change>=0) ((ImageView)rootView.findViewById(R.id.arrow)).setImageResource(R.drawable.up);
            else ((ImageView)rootView.findViewById(R.id.arrow)).setImageResource(R.drawable.down);

            ((TextView)rootView.findViewById(R.id.change)).setText(df.format(change)+" ("+df.format(changePercent)+"%)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
