package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityListSong extends AppCompatActivity{
    private FirebaseFirestore firestore;
    private ArrayAdapter<Song> adapter;
    private ListView listViewSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        firestore = FirebaseFirestore.getInstance();
        listViewSong = findViewById(R.id.listViewSong);
        final CollectionReference reference = firestore.collection("songs");

        registerForContextMenu(listViewSong);

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();
                    List<Song> list = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : snapshot){
                        Song  song = new Song();
                        song.setTitle(doc.get("title").toString());
                        list.add(song);
                    }
                    adapter = new ArrayAdapter<>(ActivityListSong.this,android.R.layout.simple_list_item_1,list);
                    listViewSong.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.listsong_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final CollectionReference reference = firestore.collection("song");
        if(item.getTitle().equals("Update")){
            final Intent intent = new Intent(ActivityListSong.this, ActivityCreateGenre.class);
            intent.putExtra("updateSong", "Update");
            reference.whereEqualTo("title", adapter.getItem(info.position).toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot doc : task.getResult()){
                            intent.putExtra("nameSong", doc.get("title").toString());
                            intent.putExtra("writerSong", doc.get("writer").toString());
                            intent.putExtra("levelSong", doc.get("level").toString());
                            intent.putExtra("genreSong", doc.get("listGenre").toString());
                            intent.putExtra("linkSong", doc.get("linkMusic").toString());
                            setResult(ActivityCreateSong.RESULT_CODE_SONG, intent);
                        }
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
        if(item.getTitle().equals("Delete")){
            Toast.makeText(this, adapter.getItem(info.position).toString(), Toast.LENGTH_SHORT).show();
            reference.whereEqualTo("title", adapter.getItem(info.position).toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            reference.document(doc.getId()).delete();
                        }
                    }
                }
            });
            finish();
            startActivity(getIntent());
            Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
}
