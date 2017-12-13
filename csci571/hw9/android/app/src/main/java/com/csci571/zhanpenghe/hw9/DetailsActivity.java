package com.csci571.zhanpenghe.hw9;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class DetailsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private boolean[] done = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private boolean[] error = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private JSONObject[] data = {null, null, null, null, null, null, null, null, null, null, null, null, null};
    private JSONArray news = null;
    private String symbol=null;

    Tab1Current tab1 = new Tab1Current();
    Tab2History tab2 = new Tab2History();
    Tab3News tab3 = new Tab3News();
    private RequestQueue queue;

    private void fetchPriceVolumeData(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_price?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[0] = true;
                                tab2.error = true;
                                error[0] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[0] = true;
                        data[0] = response;
                        tab1.done[0] = done[0];
                        tab1.data[0] = data[0];
                        tab2.data = data[0];
                        tab2.done = true;
                        tab1.updateView(0);
                        tab2.updateView();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[0] = true;
                        tab2.error = true;
                        error[0] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchSMA(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_SMA?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[1] = true;
                                error[1] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        done[1] = true;
                        data[1] = response;
                        tab1.done[1] = done[1];
                        tab1.data[1] = data[1];
                        tab1.updateView(1);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[1] = true;
                        error[1] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchEMA(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_EMA?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[2] = true;
                                error[2] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[2] = true;
                        data[2] = response;
                        tab1.done[2] = done[2];
                        tab1.data[2] = data[2];
                        tab1.updateView(2);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[2] = true;
                        error[2] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchSTOCH(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_STOCH?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[3] = true;
                                error[3] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[3] = true;
                        data[3] = response;
                        tab1.done[3] = done[3];
                        tab1.data[3] = data[3];
                        tab1.updateView(3);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[3] = true;
                        error[3] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchRSI(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_RSI?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[4] = true;
                                error[4] = true;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[4] = true;
                        data[4] = response;
                        tab1.done[4] = done[4];
                        tab1.data[4] = data[4];
                        tab1.updateView(4);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[4] = true;
                        error[4] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchADX(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_ADX?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[5] = true;
                                error[5] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[5] = true;
                        data[5] = response;
                        tab1.done[5] = done[5];
                        tab1.data[5] = data[5];
                        tab1.updateView(5);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[5] = true;
                        error[5] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchCCI(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_CCI?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[6] = true;
                                error[6] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[6] = true;
                        data[6] = response;
                        tab1.done[6] = done[6];
                        tab1.data[6] = data[6];
                        tab1.updateView(6);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[6] = true;
                        error[6] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchBBANDS(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_BBANDS?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[7] = true;
                                error[7] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[7] = true;
                        data[7] = response;
                        tab1.done[7] = done[7];
                        tab1.data[7] = data[7];
                        tab1.updateView(7);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[7] = true;
                        error[7] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void fetchMACD(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_MACD?symbol="+symbol;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.get("error")!=null){
                                tab1.error[8] = true;
                                error[8] = true;
                            }else{
                                System.out.println("<<NOT ERROR>>");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        done[8] = true;
                        data[8] = response;
                        tab1.done[8] = done[8];
                        tab1.data[8] = data[8];
                        tab1.updateView(8);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        // TODO Auto-generated method stub
                        System.out.println("FETCHING ERROR CAUGHT!\n"+err.toString());
                        tab1.error[8] = true;
                        error[8] = true;
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }



    private void getNews(){
        String url = "http://nodejs-env2.us-west-1.elasticbeanstalk.com/api/get_news?symbol="+symbol;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        done[10] = true;
                        //data[10] = response;
                        news = response;
                        tab3.doneWithNewsData(done[10], news);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        done[10] = true;
                        tab3.error = true;
                        tab3.doneWithNewsData(done[10], null);
                        System.out.println(error.toString());
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        symbol = getIntent().getStringExtra("symbol");
        getSupportActionBar().setTitle(symbol);

        tab1 = new Tab1Current();
        tab1.symbol = this.symbol.toUpperCase();
        tab2 = new Tab2History();
        tab2.symbol = this.symbol.toUpperCase();
        tab2.error = false;
        tab2.done = false;
        tab3 = new Tab3News();
        tab3.done = false;
        for(int i = 0; i<tab1.done.length; i++){
            tab1.done[i] = done[i];
            tab1.error[i] = error[i];
        }
        queue = Volley.newRequestQueue(this);

        fetchPriceVolumeData();
        fetchSMA();
        fetchEMA();
        fetchSTOCH();
        fetchRSI();
        getNews();
        fetchADX();
        fetchCCI();
        fetchBBANDS();
        fetchMACD();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    for(int i = 0; i<tab1.done.length; i++){
                        tab1.done[i] = done[i];
                        tab1.data[i] = data[i];
                    }
                    return tab1;
                case 1:
                    return tab2;
                case 2:
                    tab3.data = news;
                    tab3.done = done[10];
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "CURRENT";
                case 1:
                    return "HISTORY";
                case 2:
                    return "NEWS";
                default:
                    return null;
            }

        }
    }
}
