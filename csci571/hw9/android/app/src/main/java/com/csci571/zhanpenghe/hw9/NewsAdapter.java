package com.csci571.zhanpenghe.hw9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;

import java.net.URI;
import java.util.List;

/**
 * Created by adamhzp on 11/25/17.
 */
public class NewsAdapter extends BaseAdapter{

    private JSONArray data;
    private Context context;

    public NewsAdapter(Context context, JSONArray data){
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length();
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
        View resultView = inflater.inflate(R.layout.component, parent, false);

        TextView titleView = resultView.findViewById(R.id.newsTitle);
        TextView authorView = resultView.findViewById(R.id.author);
        TextView publishView = resultView.findViewById(R.id.publishTime);

        try{
            titleView.setText(data.getJSONObject(i).getString("title"));
            authorView.setText("Author: "+data.getJSONObject(i).getString("author"));
            publishView.setText("Date: "+data.getJSONObject(i).getString("pubDate"));
            final String url = data.getJSONObject(i).getString("link");
            resultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){

        }

        return resultView;
    }
}
