package com.example.ohsheet;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private Activity activity;
    private int layout;
    private List<Song> list;

    public SongAdapter(Activity activity, int layout, List<Song> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewName;
        TextView textViewWriter;
        ImageView imgSave;

        if(convertView== null){
           convertView = activity.getLayoutInflater().inflate(layout,null);
           textViewName = convertView.findViewById(R.id.txtName);
           textViewWriter = convertView.findViewById(R.id.txtWriter);
           imgSave = convertView.findViewById(R.id.imgSave);
           convertView.setTag(R.id.txtName,textViewName);
            convertView.setTag(R.id.txtWriter,textViewWriter);
            convertView.setTag(R.id.imgSave,imgSave);


        }else {
            textViewName = (TextView) convertView.getTag(R.id.txtName);
            textViewWriter = (TextView) convertView.getTag(R.id.txtWriter);
            imgSave = (ImageView) convertView.getTag(R.id.imgSave);
        }
        Song song = list.get(position);
        textViewName.setText(song.getName());
        textViewWriter.setText(song.getWriter());
        return convertView;
    }
}
