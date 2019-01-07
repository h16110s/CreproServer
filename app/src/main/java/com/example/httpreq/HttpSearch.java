package com.example.httpreq;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
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
import java.util.Random;

public class HttpSearch extends CustomViewer {
    String EMOTION;
    String ANIME;
    TextView textViewUrl;
    Button buttonGet;
    ListView listView;
    ArrayList<Maxim> list = new ArrayList<Maxim>();
    MyAdapter myAdapter;

    TextToSpeech tts;

    TextView idView;
    TextView animeView;
    TextView personView;
    TextView maximView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        textViewUrl = (TextView)findViewById(R.id.reqURLview);
        buttonGet = (Button)findViewById(R.id.get);
        listView = (ListView) findViewById(R.id.list);
        tts = new TextToSpeech(this, null);
        idView = (TextView)findViewById(R.id.searchID);
        animeView = (TextView)findViewById(R.id.searchAnime);
        personView = (TextView)findViewById(R.id.searchPerson);
        maximView = (TextView)findViewById(R.id.searchMaxim);
        myAdapter = new MyAdapter(HttpSearch.this);
        myAdapter.setMaximList(list);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tts.speak(list.get(position).getSpeechText(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        Intent intent = getIntent();
        EMOTION = intent.getStringExtra(HomeViewer.EXTRA_MESSAGE);
        if(EMOTION.equals("__ANIME_SEARCH")){
            ANIME = intent.getStringExtra(HomeViewer.ANIME);
            textViewUrl.setText(ANIME);
            searchAnime(ANIME);
        }
        else {
            textViewUrl.setText(EMOTION);
            searchMaxim(EMOTION);
        }
    }

    private void dispMaxim(Maxim input){
        idView.setText("ID:"+String.valueOf(input.getId()));
        animeView.setText(input.getAnime());
        personView.setText(input.getPerson());
        maximView.setText(input.getMaxim());
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

                            //検索結果が0ではなければランダムで内容を読み上げ
                            if(list.size() != 0){
                                int r = new Random().nextInt(list.size());
                                dispMaxim(list.get(r));
                                tts.speak(list.get(r).getSpeechText(),TextToSpeech.QUEUE_FLUSH, null);
                            }
                            else{
                                dispMaxim(new Maxim(0,"Can't get Maxim","","",""));
                                tts.speak("該当する名言は見つかりませんでした。",TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                } catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET","Connection Refused:onButtonGet");
                            list.clear();
//                            list.add(new Maxim(0,"Connection Refused","","",""));
                            myAdapter.notifyDataSetChanged();
                            dispMaxim(new Maxim(0,"Connection Refused","","",""));
                        }
                    });
                }
            }
        }).start();
    }


    public void searchAnime(String anime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL( getAddress() + "/anime/" + ANIME);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    final String str = InputStreamToString(con.getInputStream());
                    Log.d("HTTP", str);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET",str);
                            addToList(str);
                            myAdapter.notifyDataSetChanged();

                            //検索結果が0ではなければランダムで内容を読み上げ
                            if(list.size() != 0){
                                int r = new Random().nextInt(list.size());
                                dispMaxim(list.get(r));
                                tts.speak(list.get(r).getSpeechText(),TextToSpeech.QUEUE_FLUSH, null);
                            }
                            else{
                                dispMaxim(new Maxim(0,"Can't get Maxim","","",""));
                                tts.speak("該当するアニメは見つかりませんでした。",TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                } catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HTTP_GET","Connection Refused:onButtonGet");
                            list.clear();
//                            list.add(new Maxim(0,"Connection Refused","","",""));
                            myAdapter.notifyDataSetChanged();
                            dispMaxim(new Maxim(0,"Connection Refused","","",""));
                        }
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
