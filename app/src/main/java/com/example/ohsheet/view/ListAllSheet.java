package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ListAllSheet extends AppCompatActivity {
    private ListView listView;
    private List<Song> list;
    private SongAdapter songAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_sheet);
        listView = findViewById(R.id.listView);
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        final CollectionReference reference = firestore.collection("songs");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                doc.get("content").toString()
                        );
                        list.add(song);
                    }
                    songAdapter = new SongAdapter(ListAllSheet.this,R.layout.customlayout,list);
                    listView.setAdapter(songAdapter);
                }
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