

package com.example.ohsheet.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohsheet.R;
import com.example.ohsheet.adapter.ChordAdapter;
import com.example.ohsheet.entity.Chord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Chord> list;
    private FirebaseFirestore firestore;
    private ChordAdapter adapter;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        final CollectionReference reference = firestore.collection("chords");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    list = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : snapshots){
                        Chord chord = new Chord(doc.get("name").toString(),doc.get("link").toString());
                        list.add(chord);
                    }
                    adapter = new ChordAdapter(ChordActivity.this, list);
                    arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }
}