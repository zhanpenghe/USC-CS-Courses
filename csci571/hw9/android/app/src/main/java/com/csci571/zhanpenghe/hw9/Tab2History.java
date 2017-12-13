package com.csci571.zhanpenghe.hw9;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by adamhzp on 11/25/17.
 */

public class Tab2History extends Fragment{

    boolean done = false;
    boolean error = false;
    JSONObject data = null;

    String symbol;
    ProgressBar historyProgress;
    WebView histock;
    View rootView;
    TextView errorMsg;

    public void updateView(){
        try{
            if(done && !error){
                historyProgress.setVisibility(View.INVISIBLE);
                histock.getSettings().setJavaScriptEnabled(true);
                histock.loadUrl("file:///android_asset/html/history.html");
                histock.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url){
                        try {
                            String hi_stock_ = data.getString("hi_stock_data");
                            histock.loadUrl("javascript:setChart(" + hi_stock_ + ", '"+symbol+"')");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                histock.setVisibility(View.VISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);
            }else if(!done){
                historyProgress.setVisibility(View.VISIBLE);
                histock.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);
            }else{
                errorMsg.setVisibility(View.VISIBLE);
                histock.setVisibility(View.INVISIBLE);
                historyProgress.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2history, container, false);
        historyProgress = rootView.findViewById(R.id.historyProgress);
        histock = rootView.findViewById(R.id.history);
        errorMsg = rootView.findViewById(R.id.history_error);
        updateView();
        return rootView;
    }
}
