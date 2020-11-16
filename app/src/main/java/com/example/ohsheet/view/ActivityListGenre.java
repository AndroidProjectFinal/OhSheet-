package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private ArrayAdapter<Genre> adapter;
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
                    adapter = new ArrayAdapter<>(ActivityListGenre.this,android.R.layout.simple_list_item_1,list);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle().equals("Update")){
            Intent intent = new Intent(ActivityListGenre.this, ActivityCreateGenre.class);
            intent.putExtra("updateGenre", "Update");
            intent.putExtra("genreName", adapter.getItem(info.position).toString());
            startActivity(intent);
            finish();
        }
        if(item.getTitle().equals("Delete")){
            final CollectionReference reference = firestore.collection("genre");
            Toast.makeText(this, adapter.getItem(info.position).toString(), Toast.LENGTH_SHORT).show();
            reference.whereEqualTo("genreName", adapter.getItem(info.position).toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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