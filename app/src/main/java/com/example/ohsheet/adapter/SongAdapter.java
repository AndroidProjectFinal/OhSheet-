package com.example.ohsheet.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Song;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private Activity activity;
    private int layout;
    private List<Song> list;
    private int state;
     private String infoUser;
    public SongAdapter(Activity activity, int layout, List<Song> list,String s) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        this.infoUser = s;
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
        final ImageView imgSave;
        final TextView txtLike;
        if(convertView== null){
           convertView = activity.getLayoutInflater().inflate(layout,null);
           textViewName = convertView.findViewById(R.id.txtName);
           textViewWriter = convertView.findViewById(R.id.txtWriter);
           imgSave = convertView.findViewById(R.id.imgSave);
           txtLike = convertView.findViewById(R.id.txtLike);
           convertView.setTag(R.id.txtName,textViewName);
           convertView.setTag(R.id.txtWriter,textViewWriter);
           convertView.setTag(R.id.imgSave,imgSave);
           convertView.setTag(R.id.txtLike,txtLike);
        }else {
            textViewName = (TextView) convertView.getTag(R.id.txtName);
            textViewWriter = (TextView) convertView.getTag(R.id.txtWriter);
            imgSave = (ImageView) convertView.getTag(R.id.imgSave);
            txtLike = (TextView) convertView.getTag(R.id.txtLike);
        }

        final Song song = list.get(position);
        textViewName.setText(song.getTitle());
        textViewWriter.setText(song.getWriter());
        txtLike.setText(Integer.toString(song.getLikeQuantity()));
        imgSave.setImageResource(R.drawable.star);
        imgSave.setTag(R.drawable.star);
        state = Integer.parseInt(imgSave.getTag().toString());

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!infoUser.equalsIgnoreCase("Oh Sheet")) {
                    Log.d("og", infoUser + "acbed");
                    if (state == R.drawable.star) {
                        imgSave.setImageResource(R.drawable.starv);
                        Log.d("og", infoUser);
                        imgSave.setTag(R.drawable.starv);
                        state = Integer.parseInt(imgSave.getTag().toString());
                        txtLike.setText(Integer.toString(song.getLikeQuantity() + 1));

                    }else if (state == R.drawable.starv) {
                        imgSave.setImageResource(R.drawable.star);
                        imgSave.setTag(R.drawable.star);
                        state = Integer.parseInt(imgSave.getTag().toString());
                        txtLike.setText(Integer.toString(song.getLikeQuantity()));

                    }
                }
            }
        });

        return convertView;
    }
}
