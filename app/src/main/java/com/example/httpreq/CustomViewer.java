package com.example.httpreq;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CustomViewer extends AppCompatActivity {
    TextView serverIP;
    EditText newServerIP;
    EditText newServerPort;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
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

    public String getAddress(){
        return sharedPreferences.getString("ADDRESS", "http://192.168.10.100:3000");
    }


    private void showIPDialog(final Activity activity){
        android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        //カスタムダイアログの要素にアクセスするためのView
        View dig = inflater.inflate(R.layout.dialog_ip, null);
        serverIP = (TextView) dig.findViewById(R.id.currentServer);
        serverIP.setText("現在の接続先："+ getAddress());
        newServerIP = (EditText) dig.findViewById(R.id.newServer);
        newServerPort = (EditText) dig.findViewById(R.id.newServerPort);
        ad.setView(dig)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (!newServerIP.getText().toString().isEmpty()) {
                            String address = "http://" + newServerIP.getText().toString();

                            if (!newServerPort.getText().toString().isEmpty()) {
                                address += ":" + newServerPort.getText().toString();
                                editor.putString("ADDRESS",address);
                                editor.commit();
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
