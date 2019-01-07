package com.example.httpreq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.*;

public class HomeViewer extends CustomViewer {
    public static final String EXTRA_MESSAGE = "com.example.HomeViewer.EXTRA_MESSAGE";
    public static final String ANIME="com.example.HomeViewer.ANIME";
    private static final int REQUEST_CODE = 1234;
//    private static final Set<String> EMOTION = new HashSet<String>(Arrays.asList("元気","別れ","悲し","挫折","感動", "暇","動作テスト"));
    private static final Map<String, String> EMOTION = new HashMap<String, String>();
    private static final Map<String, String> ANIME_MAP = new HashMap<>();
    TextView textView;
    public TextToSpeech tts;
    ImageView matchImage;

    //MSerch 名言の検索を行うための音声検索ボタン
    //resultM 名言の検索の結果を表示するTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        setMap();
        makeAnimeMap();
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home_viewer);
        textView = (TextView)findViewById(R.id.resultM);
        tts = new TextToSpeech(this, null);


        matchImage = findViewById(R.id.match_image);
//        matchImage.setVisibility(View.VISIBLE);

    }

    public void showDialog(final Activity activity, String title, String text){
        AlertDialog.Builder ad =new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                activity.setResult(Activity.RESULT_OK);
            }
        });
        ad.create();
        ad.show();
    }

    public void MSerch(View v) {
        matchImage.setVisibility(View.INVISIBLE);
        textView.setText("");

        //Intentの作成
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //表示させる文字列
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を文字で出力します");
        //Intent開始
        startActivityForResult(intent, 1234);//第2引数はrequestCode
    }

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data){
        String spokenString ="";
        Intent intent = new Intent(this, HttpSearch.class);
        //自分で投げたIntentであれば応答
        if(reqestCode == 1234 && resultCode == RESULT_OK){
            //すべての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //認識結果が複数の場合、１つに結合
            for(int i = 0; i<speechToChar.size(); i++){
                spokenString += speechToChar.get(i) + "\n";
            }

            if(EMOTION.containsKey(speechToChar.get(0))) {
                intent.putExtra(EXTRA_MESSAGE, EMOTION.get(speechToChar.get(0)));
                startActivity(intent);
            }
            else if(ANIME_MAP.containsKey(speechToChar.get(0))){
                intent.putExtra(EXTRA_MESSAGE,"__ANIME_SEARCH");
                intent.putExtra(ANIME,ANIME_MAP.get(speechToChar.get(0)));
                startActivity(intent);
            }
            else{
                Glide.with(this).load(R.raw.kesshouban).into(matchImage);
                textView.setText("あのね，あのね，それ検索できないの！　by血小板ちゃん はたらく細胞より");

                matchImage.setVisibility(View.VISIBLE);
            }
        }else{
            textView.setText("音声認識サービスに接続できませんでした．");
        }
//        tts.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
        //Toastを用いて結果の表示
        //Toast.makeText(this, spokenString, Toast.LENGTH_LONG).show();
        //Dialogを用いて表示
//        showDialog(this,"",spokenString);
//        super.onActivityResult(reqestCode,resultCode,data);//お決まりだそうです
    }

    private void setMap(){
        EMOTION.put("元気","元気");
        EMOTION.put("元気にして","元気");
        EMOTION.put("元気が出ない","元気");
        EMOTION.put("元気が出る名言","元気");
        EMOTION.put("元気が欲しい","元気");
        EMOTION.put("悲しい","悲し");
        EMOTION.put("悲しい気分","悲し");
        EMOTION.put("悲しいな","悲し");
        EMOTION.put("悲しみ","悲し");
        EMOTION.put("悲しみが深い","悲し");
        EMOTION.put("泣きそう","悲し");
        EMOTION.put("挫折","挫折");
        EMOTION.put("心が折れた","挫折");
        EMOTION.put("無理","挫折");
        EMOTION.put("感動","感動");
        EMOTION.put("やばす","感動");
        EMOTION.put("やばい","感動");
        EMOTION.put("暇","暇");
        EMOTION.put("なんか面白いこと言って","暇");
        EMOTION.put("動作テスト","動作テスト");
    }

    private void makeAnimeMap(){
        ANIME_MAP.put("天空の城ラピュタ","天空の城ラピュタ");
        ANIME_MAP.put("ルパン三世","ルパン三世");
        ANIME_MAP.put("ロウきゅーぶ","ロウきゅーぶ！");
        ANIME_MAP.put("七つの大罪","七つの大罪");
        ANIME_MAP.put("三月のライオン","三月のライオン");
        ANIME_MAP.put("北斗の拳","北斗の拳");
        ANIME_MAP.put("嘘喰い","嘘喰い");
        ANIME_MAP.put("四月は君の嘘","四月は君の嘘");
        ANIME_MAP.put("宇宙兄弟","宇宙兄弟");
        ANIME_MAP.put("巨人の星","巨人の星");
        ANIME_MAP.put("東京喰種","東京喰種");
        ANIME_MAP.put("東京グール","東京喰種");
        ANIME_MAP.put("機動戦士ガンダム","機動戦士ガンダム");
        ANIME_MAP.put("ガンダム","機動戦士ガンダム");
        ANIME_MAP.put("荒川アンダーザブリッジ","荒川アンダーザブリッジ");
        ANIME_MAP.put("蟲師","蟲師");
        ANIME_MAP.put("西遊記","西遊記");
        ANIME_MAP.put("賭博黙示録カイジ","賭博黙示録カイジ");
        ANIME_MAP.put("カイジ","賭博黙示録カイジ");
        ANIME_MAP.put("進撃の巨人","進撃の巨人");
        ANIME_MAP.put("銀の匙","銀の匙");
        ANIME_MAP.put("銀魂","銀魂");
        ANIME_MAP.put("鋼の錬金術師","鋼の錬金術師");
        ANIME_MAP.put("アリア","ARIA");
        ANIME_MAP.put("BLEACH","BLEACH");
        ANIME_MAP.put("FAIRY TAIL","FAIRY TAIL");
        ANIME_MAP.put("メジャー","MAJOR");
        ANIME_MAP.put("ワンピース","ONE PIECE");
        ANIME_MAP.put("スラムダンク","SLAM DANK");
        ANIME_MAP.put("しらん","しらん");
        ANIME_MAP.put("とらドラ","とらドラ！");
        ANIME_MAP.put("ど根性ガエル","ど根性ガエル");
        ANIME_MAP.put("のだめカンタービレ","のだめカンタービレ");
        ANIME_MAP.put("サマーウォーズ","サマーウォーズ");
        ANIME_MAP.put("シュタインズゲート","シュタインズ・ゲート");
        ANIME_MAP.put("シュタインズ ゲート","シュタインズ・ゲート");
        ANIME_MAP.put("セーラームーン","セーラームーン");
        ANIME_MAP.put("タッチ","タッチ");
        ANIME_MAP.put("ドラえもん","ドラえもん");
        ANIME_MAP.put("バカボンド","バカボンド");
        ANIME_MAP.put("バクマン。","バクマン。");
        ANIME_MAP.put("ラブひな","ラブひな");
    }
}


