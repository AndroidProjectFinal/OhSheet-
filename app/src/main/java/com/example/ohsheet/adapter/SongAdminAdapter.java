package com.example.ohsheet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Song;

import java.util.List;

public class SongAdminAdapter extends BaseAdapter {

    private Activity activity;
    private int layout;
    private List<Song> list;

    public SongAdminAdapter(Activity activity, int layout, List<Song> list) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView textView;
        if(view == null){
            view = activity.getLayoutInflater().inflate(layout,null);
            textView = view.findViewById(R.id.textNameSongAdmin);

        }else {
            textView = (TextView) view.getTag(R.id.textNameSongAdmin);

        }

        Song song = list.get(position);
        textView.setText(song.getTitle());
        return view;
    }
}
