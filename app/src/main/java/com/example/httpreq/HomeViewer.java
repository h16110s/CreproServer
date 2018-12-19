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
    public static final String EXTRA_MESSAGE = "";
    private static final int REQUEST_CODE = 1234;
    private static final Set<String> EMOTION = new HashSet<String>(Arrays.asList("元気","別れ","悲し","挫折","感動", "暇","動作テスト"));
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
        textView.setText("今の気持ちを教えてね");
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
        //自分で投げたIntentであれば応答
        if(reqestCode == 1234 && resultCode == RESULT_OK){
            //すべての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //認識結果が複数の場合、１つに結合
            for(int i = 0; i<speechToChar.size(); i++){
                spokenString += speechToChar.get(i) + "\n";
            }
            if(EMOTION.contains(speechToChar.get(0))) {
                Intent intent = new Intent(this, HttpSearch.class);
                intent.putExtra(EXTRA_MESSAGE, speechToChar.get(0).toString());
                startActivity(intent);

            }
            else{
                Glide.with(this).load(R.raw.kesshouban).into(matchImage);
                textView.setText("あのね，あのね，その感情は検索できないの！　by血小板ちゃん はたらく細胞より");
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
}


