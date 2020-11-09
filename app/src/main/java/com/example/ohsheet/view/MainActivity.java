package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ohsheet.R;
import com.example.ohsheet.adapter.GenreAdapter;
import com.example.ohsheet.entity.Genre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GenreAdapter.OnItemClicked {

    private RecyclerView mRecyclerView;
    private List<Genre> listGenre;
    private GenreAdapter adapter;
    private FirebaseFirestore firestore;
    private ImageView expandedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        expandedMenu = findViewById(R.id.expandedMenu);


        firestore = FirebaseFirestore.getInstance();
        final CollectionReference reference = firestore.collection("genre");

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                     QuerySnapshot snapshots = task.getResult();
                     listGenre = new ArrayList<>();
                     for(QueryDocumentSnapshot doc : snapshots){
                            Genre genre = new Genre(
                                    doc.get("name").toString(),
                                    doc.get("link").toString()
                            );
                            listGenre.add(genre);
                     }
                     adapter = new GenreAdapter(MainActivity.this, listGenre, MainActivity.this);
                     mRecyclerView.setAdapter(adapter);
                }
            }
        });


        expandedMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdmin.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onItemClick(int position) {
        Log.d("TAG", "Clicked - " + position);
        Intent intent = new Intent(getApplicationContext(), ListAllSheet.class);
        startActivity(intent);
    }



}