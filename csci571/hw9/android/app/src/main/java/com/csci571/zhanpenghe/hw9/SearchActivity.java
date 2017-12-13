package com.csci571.zhanpenghe.hw9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {

    //View Objects
    AutoCompleteTextView autoComplete;
    Spinner order;
    Spinner sortBy;
    ListView favList;
    int longPressedID;

    private RequestQueue queue;
    Timer timer;

    FavoriteListAdapter favAdapter;
    AutoCompleteAdapter acAdapter;
    ProgressBar bar;
    ProgressBar favListBar;

    ImageButton updateButton;

    public void setProgressBar(boolean isVisible){
        bar.setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
    }

    public void setFavProgress(boolean isVisible){
        favListBar.setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bar = findViewById(R.id.searchViewProgress);
        bar.setVisibility(View.INVISIBLE);
        //Auto complete
        autoComplete = findViewById(R.id.autoCompleteTextView);
        autoComplete.setThreshold(1);
        acAdapter=new AutoCompleteAdapter(this, android.R.layout.select_dialog_item);
        autoComplete.setAdapter(acAdapter);
        autoComplete.setTextColor(Color.WHITE);

        autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = autoComplete.getText().toString();
                if(!text.contains("-") && isValid(text)) fetchAutoCompleteData(text);
                else{
                    setProgressBar(false);
                    acAdapter.setData(new ArrayList<String>());
                }
            }
        });

        queue = Volley.newRequestQueue(this);
        order = findViewById(R.id.order);
        sortBy = findViewById(R.id.sort_by);
        favList = findViewById(R.id.fav_list);
        favListBar = findViewById(R.id.favoriteProgress);
        favListBar.setVisibility(View.INVISIBLE);
        favAdapter = new FavoriteListAdapter(getApplicationContext(), this);
        favList.setAdapter(favAdapter);

        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavProgress(true);
                if(favAdapter.getData().size()>0) fetchFavoriteListData();
                else setFavProgress(false);
            }
        });

        ArrayList<String> orderList = new ArrayList<>();
        orderList.add("Order");
        orderList.add("Ascending");
        orderList.add("Descending");

        final SAdapter orderAdapter = new SAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderList, "Order");
        order.setAdapter(orderAdapter);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String orderStr = order.getSelectedItem().toString();
                String sortByStr = sortBy.getSelectedItem().toString();
                favAdapter.reorder(sortByStr, orderStr);
                orderAdapter.setCurrent(orderStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> sortByList = new ArrayList<>();
        sortByList.add("Sort by");
        sortByList.add("Default");
        sortByList.add("Price");
        sortByList.add("Symbol");
        sortByList.add("Change");
        sortByList.add("Change (%)");


        final SAdapter sortByAdapter = new SAdapter(this, android.R.layout.simple_spinner_dropdown_item, sortByList, "Order");
        sortBy.setAdapter(sortByAdapter);


        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String orderStr = order.getSelectedItem().toString();
                String sortByStr = sortBy.getSelectedItem().toString();
                favAdapter.reorder(sortByStr, orderStr);
                sortByAdapter.setCurrent(sortByStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Switch autoRefresh = findViewById(R.id.auto_refresh);
        autoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    startAutoRefresh();
                }else{
                    stopAutoRefresh();
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        favAdapter.reloadData();
        if(favAdapter.getData().size()>0) {
            setFavProgress(true);
            fetchFavoriteListData();
        }
    }

    public void clear(View view) {
        autoComplete.setText("");
    }

    private boolean isValid(String str){
        if(str == null) return false;
        if(str.equalsIgnoreCase("")) return false;
        boolean result = false;
        for(int i = 0; i<str.length(); i++){
            if (!Character.isWhitespace(str.charAt(i))) return true;
        }
        return false;
    }

    private String getSymbol(String str){
        if(str.indexOf('-')>=0){
            String[] infos = str.split("[-]");
            return infos[0].trim();
        }else{
            return str.trim();
        }
    }

    public void search(View view){
        String symbol = autoComplete.getText().toString();
        if(!isValid(symbol)){
            Context context = getApplicationContext();
            CharSequence text = "Please enter a stock name or symbol";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("symbol", getSymbol(symbol));
            startActivity(intent);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Remove from Favorites?");
        //LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //View view = inflater.inflate(R.layout.context_title, null);
        menu.add(Menu.NONE, 0, 0, "No");
        menu.add(Menu.NONE, 1, 1, "Yes");
        //menu.setHeaderView(view);
        longPressedID = v.getId();
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast toast = Toast.makeText(getApplicationContext(), "Selected No", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 1:
                favAdapter.remove(this.longPressedID);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Selected Yes", Toast.LENGTH_SHORT);
                toast1.show();
                break;
            default:
                return false;
        }
        return true;
    }


    public void fetchFavoriteListData(){
        List<JSONObject> objs = favAdapter.getData();

        for(int i =0; i<objs.size(); i++){
            try{
                JSONObject obj = objs.get(i);
                final String sym = obj.getString("symbol");
                String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_meta?symbol="+sym;
                final int index = i;
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                                favAdapter.done(index, response, sym);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                                Toast toast = Toast.makeText(getApplicationContext(), "Failed to update "+sym+" data.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsObjRequest);

            }catch (Exception e){
            }
        }
    }

    public void fetchAutoCompleteData(String text){
        setProgressBar(true);
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/auto_complete?symbol=" + text;
        acAdapter.setData(new ArrayList<String>());
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> results = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            if (i >= 5) break;
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String value = obj.get("Symbol") + " - " + obj.get("Name") + " (" + obj.get("Exchange") + ")";
                                results.add(value);
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                        }
                        setProgressBar(false);
                        acAdapter.setData(results);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setProgressBar(false);
                        System.out.println(error.toString());
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }


    public void startAutoRefresh(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFavProgress(true);
                    }
                });
                if(favAdapter.getData().size()>0) {
                    fetchFavoriteListData();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setFavProgress(false);
                        }
                    });
                }
            }
        }, 0, 10000);
    }

    public void stopAutoRefresh(){
        timer.cancel();
        timer.purge();
        setFavProgress(false);
    }


}
