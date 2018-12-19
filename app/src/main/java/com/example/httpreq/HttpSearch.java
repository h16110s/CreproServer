package com.example.httpreq;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.EmbossMaskFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class HttpSearch extends AppCompatActivity {

    TextView textViewUrl;
    TextView serverIP;
    EditText newServerIP;
    EditText newServerPort;
    Button buttonGet;
    ListView listView;
    ArrayList<Maxim> list = new ArrayList<Maxim>();
    MyAdapter myAdapter;
    String address;
    String EMOTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        address="http://192.168.10.100:3000";
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
    }

    public void searchMaxim(String emotion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(address+"emotion/"+EMOTION);
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
                    System.out.println(ex);
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
                    URL url = new URL(address);
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
                    System.out.println(ex);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 参照するリソースは上でリソースファイルに付けた名前と同じもの
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem1:
                showIPDialog(this);
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showIPDialog(final Activity activity){
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        //カスタムダイアログの要素にアクセスするためのView
        View dig = inflater.inflate(R.layout.dialog_ip, null);
        serverIP = (TextView) dig.findViewById(R.id.currentServer);
        serverIP.setText("現在の接続先："+address);
        newServerIP = (EditText) dig.findViewById(R.id.newServer);
        newServerPort = (EditText) dig.findViewById(R.id.newServerPort);
        ad.setView(dig)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (!newServerIP.getText().toString().isEmpty()) {
                            address = "http://" + newServerIP.getText().toString();
                            if (!newServerPort.getText().toString().isEmpty()) {
                                address += ":" + newServerPort.getText().toString();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        ad.create();
        ad.show();
    }


}
