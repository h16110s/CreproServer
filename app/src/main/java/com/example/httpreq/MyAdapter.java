package com.example.httpreq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context contexit;
    LayoutInflater layoutInflater = null;
    ArrayList<Maxim> maximList;


    public MyAdapter(Context context){
        this.contexit = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setMaximList(ArrayList<Maxim> maximList){
        this.maximList = maximList;
    }

    @Override
    public int getCount() {
        return maximList.size();
    }

    @Override
    public Object getItem(int position) {
        return maximList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return maximList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.maxim_list_layout,parent,false );

        TextView tmp;

        tmp = (TextView) convertView.findViewById(R.id.id);
        tmp.setText("ID: "+String.valueOf(maximList.get(position).getId()));

        tmp = (TextView) convertView.findViewById(R.id.maxim);
        tmp.setText(String.valueOf(maximList.get(position).getMaxim()));

        tmp = (TextView) convertView.findViewById(R.id.person);
        tmp.setText(String.valueOf(maximList.get(position).getPerson()));

        tmp = (TextView) convertView.findViewById(R.id.anime);
        tmp.setText(String.valueOf(maximList.get(position).getAnime()));

        return  convertView;
    }
}
