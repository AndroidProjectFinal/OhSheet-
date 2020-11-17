package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ohsheet.R;
import com.example.ohsheet.adapter.SongAdapter;
import com.example.ohsheet.entity.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListSheetbyGenre extends AppCompatActivity {
    private ListView listView;
    private List<Song> list;
    private SongAdapter songAdapter;
    private FirebaseFirestore firestore;
    private EditText txtSearch;
    private ImageView imgSearch;
    private List<Song> list2;
    private String text;
    private int genID;
    private String info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sheetby_genre);
        listView = findViewById(R.id.listSheetGenre);
        firestore = FirebaseFirestore.getInstance();
        txtSearch = findViewById(R.id.txtSearchSongs);
        imgSearch = findViewById(R.id.imageViewbyGenre);
        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        info = bundle.getString("info");
        genID = bundle.getInt("genre");
        firestore.collection("songs").whereEqualTo("listGenre",genID)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    for(QueryDocumentSnapshot doc : snapshots){
                        Song song = new Song(
                                doc.get("title").toString(),
                                doc.get("writer").toString(),
                                doc.get("sheet").toString(),
                                doc.get("linkMusic").toString(),
                                doc.get("content").toString(),
                                doc.getLong("likeQuantity").intValue()
                        );
                        list.add(song);
                    }
                    songAdapter = new SongAdapter(ListSheetbyGenre.this,R.layout.customlayout,list,info);
                    listView.setAdapter(songAdapter);
                }
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list2= new ArrayList<>();
                firestore.collection("songs").whereEqualTo("listGenre",genID)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot snapshots = task.getResult();
                            for(QueryDocumentSnapshot doc : snapshots){
                                Song song = new Song(
                                        doc.get("title").toString(),
                                        doc.get("writer").toString(),
                                        doc.get("sheet").toString(),
                                        doc.get("linkMusic").toString(),
                                        doc.get("content").toString(),
                                        doc.getLong("likeQuantity").intValue()
                                );
                                list.add(song);
                            }
                            text = txtSearch.getText().toString();
                            for(int i =0;i<list.size();i++){
                                if(list.get(i).getTitle().trim().contains(text.trim())){
                                    Log.d("og",text+"acb");
                                    list2.add(list.get(i));
                                }
                            }

                            songAdapter = new SongAdapter(ListSheetbyGenre.this,R.layout.customlayout,list2,info);
                            listView.setAdapter(songAdapter);
                        }
                    }
                });

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song =(Song) parent.getAdapter().getItem(position);

                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("title", song.getTitle());
                intent.putExtra("writer", song.getWriter());
                intent.putExtra("sheet", song.getSheet());
                intent.putExtra("link", song.getLinkMusic());
                intent.putExtra("content", song.getContent());
                startActivity(intent);
            }
        });
    }
    }
