package com.example.ohsheet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.ohsheet.adapter.SongAdapter;
import com.example.ohsheet.entity.Song;

import java.util.ArrayList;
import java.util.List;

//public class ListAllSheet extends AppCompatActivity {
//    private ListView listView;
//    private List<Song> list;
//    private SongAdapter songAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_all_sheet);
//        listView = findViewById(R.id.listView);
//        list = new ArrayList<>();
//        list.add(new Song(1,"Hoa nở không màu","Hoài Lâm"));
//        list.add(new Song(2,"Nàng thơ","Hoàng Dũng"));
//        list.add(new Song(3,"Missing You","G-Dragon"));
//        songAdapter = new SongAdapter(this,R.layout.customlayout,list);
//        listView.setAdapter(songAdapter);
//
//
//    }
//}