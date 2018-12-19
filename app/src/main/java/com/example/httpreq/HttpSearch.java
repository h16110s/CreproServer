package com.example.httpreq;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpSearch extends CustomViewer {
    TextView textViewUrl;
    Button buttonGet;
    ListView listView;
    ArrayList<Maxim> list = new ArrayList<Maxim>();
    MyAdapter myAdapter;
    String EMOTION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        textViewUrl = (TextView)findViewById(R.id.reqURLview);
        buttonGet = (Button)findViewById(R.id.get);
        listView = (ListView) findViewById(R.id.list);
        myAdapter = new MyAdapter(HttpSearch.this);
        myAdapter.setMaximList(list);
        listView.setAdapter(myAdapter);
        list.add(new Maxim(0, "さぁ、ゲームをはじめよう","ノーゲーム・ノーライフ","『　』","暇"));

        Intent intent = getIntent();
        EMOTION = intent.getStringExtra(HomeViewer.EXTRA_MESSAGE);
        textViewUrl.setText(EMOTION);
        searchMaxim(EMOTION);
    }

    public void searchMaxim(String emotion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL( getAddress() + "/emotion/" + EMOTION);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    final String str = InputStreamToString(con.getInputStream());
                    Log.d("HTTP", str);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET",str);
                            addToList(str);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET","Connection Refused:onButtonGet");
                            list.clear();
                            list.add(new Maxim(0,"Connection Refused","","",""));
                            myAdapter.notifyDataSetChanged();                        }
                    });
                }
            }
        }).start();
    }

    void addToList(String str){
        list.clear();
        try {
            JSONArray maximArray = new JSONArray(str);
            for(int i = 0; i < maximArray.length(); i++){
                JSONObject tmp = (JSONObject) maximArray.get(i);
                list.add(new Maxim(tmp.getInt("id"),
                        tmp.getString("maxim"),
                        tmp.getString("anime"),
                        tmp.getString("person"),
                        tmp.getString("emotion")));
            }
        } catch (JSONException e) {
            Log.e("HTTP-JSON",e.toString());
        }
    }

    public void onButtonGet(View view) {
        Toast.makeText(this,"click test",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(getAddress());
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    final String str = InputStreamToString(con.getInputStream());
                    Log.d("HTTP", str);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET",str);
                            addToList(str);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET","Connection Refused:onButtonGet");
                            list.clear();
                            list.add(new Maxim(0,"Connection Refused","","",""));
                            myAdapter.notifyDataSetChanged();                        }
                    });
                }
            }
        }).start();
    }

    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
