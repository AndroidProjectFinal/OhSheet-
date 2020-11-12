package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityListGenre extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private ListView listViewGenre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_genre);

        firestore = FirebaseFirestore.getInstance();
        listViewGenre = findViewById(R.id.listViewGenre);
        final CollectionReference reference = firestore.collection("genre");

        registerForContextMenu(listViewGenre);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();
                    List<Genre> list = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : snapshot){
                        Genre  genre = new Genre();
                        genre.setGenreName(doc.get("genreName").toString());
                        list.add(genre);
                    }
                    ArrayAdapter<Genre> adapter = new ArrayAdapter<>(ActivityListGenre.this,android.R.layout.simple_list_item_1,list);
                    listViewGenre.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.listgenre_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

}