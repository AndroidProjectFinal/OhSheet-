package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.adapter.GenreAdapter;
import com.example.ohsheet.entity.Genre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GenreAdapter.OnItemClicked, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private List<Genre> listGenre;
    private GenreAdapter adapter;
    private FirebaseFirestore firestore;
    private ImageView expandMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Phần search
            }
        });

        drawerLayout = findViewById(R.id.drawLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Nút trên góc trái màn hình
        expandMenu = findViewById(R.id.expandedMenu);
        expandMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        //Load dữ liệu genre lên màn hình chính
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
                                    doc.get("genreName").toString(),
                                    doc.get("genreImg").toString()
                            );
                            listGenre.add(genre);
                     }
                     adapter = new GenreAdapter(MainActivity.this, listGenre, MainActivity.this);
                     mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    //Ấn các item trên màn hình chính
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Clicked - " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ListAllSheet.class);
        startActivity(intent);
    }

    //Ấn các item ở bên trái khi vuốt màn hình
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            //Vô các bài hát
            case R.id.nav_songs:
                Toast.makeText(this, "Clicked songs", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(),ListAllSheet.class);
                startActivity(intent2);
                break;
            //Vô các bài hát yêu thích
            case R.id.nav_favor:
                Toast.makeText(this, "Clicked Favor", Toast.LENGTH_SHORT).show();
                break;
            //Vô các hợp âm
            case R.id.nav_chord:
                Toast.makeText(this, "Clicked Chord", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(),ChordActivity.class);
                startActivity(intent1);
                break;
            //Vô login
            case R.id.nav_login:
                Intent intent = new Intent(getApplicationContext(), ActivityAdmin.class);
                startActivity(intent);
                Toast.makeText(this, "Clicked Login", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }
}