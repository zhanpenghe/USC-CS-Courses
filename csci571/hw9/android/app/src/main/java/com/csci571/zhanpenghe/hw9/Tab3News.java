package com.csci571.zhanpenghe.hw9;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by adamhzp on 11/25/17.
 */

public class Tab3News extends Fragment{

    boolean done = false;
    boolean error = false;

    JSONArray data = null;
    View rootView;
    ListView list;
    ProgressBar newsProgress;
    TextView errorMSG;


    public void doneWithNewsData(boolean done, JSONArray obj){
        this.done = done;
        this.data = obj;
        updateView();
    }

    private void updateView(){
        try {
            if (done && !error) {
                newsProgress.setVisibility(View.INVISIBLE);
                list.setAdapter(new NewsAdapter(getContext(), data));
                list.setVisibility(View.VISIBLE);
                errorMSG.setVisibility(View.INVISIBLE);
            } else if(!done) {
                newsProgress.setVisibility(View.VISIBLE);
                list.setVisibility(View.INVISIBLE);
                errorMSG.setVisibility(View.INVISIBLE);
            } else{
                errorMSG.setVisibility(View.VISIBLE);
                list.setVisibility(View.INVISIBLE);
                newsProgress.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab3news, container, false);
        newsProgress = rootView.findViewById(R.id.newsProgress);
        list = rootView.findViewById(R.id.news);
        errorMSG = rootView.findViewById(R.id.news_error);

        updateView();
        return rootView;
    }


}
