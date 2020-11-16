package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.adapter.GenreAdapter;
import com.example.ohsheet.adapter.SongAdapter;
import com.example.ohsheet.entity.Genre;
import com.example.ohsheet.entity.Song;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
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
    private TextView textViewNav;
    private MenuItem menuItem;
    private ListAllSheet listAllSheet;
    private SongAdapter songAdapter;

    public static int REQUEST_CODE= 100;
    public static int RESULT_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listAllSheet = new ListAllSheet();
        drawerLayout = findViewById(R.id.drawLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_login);
        View header = navigationView.getHeaderView(0);
        textViewNav = header.findViewById(R.id.textViewNavHeader);



        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Nút trên góc trái màn hình
        expandMenu = findViewById(R.id.expandedMenu);
        expandMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
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
        Intent intent = new Intent(getApplicationContext(),ListSheetbyGenre.class);
        intent.putExtra("genre",position);
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

                if(!textViewNav.getText().equals("Oh Sheet")){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    menuItem.setTitle("Login");
                    textViewNav.setText("Oh Sheet");
                    Toast.makeText(this, "Clicked LogOut", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ActivityAdmin.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    Toast.makeText(this, "Clicked Login", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == RESULT_CODE){
            String userName = data.getStringExtra("userName");
            Log.i("AAAAAAAAAAA", userName);
            textViewNav.setText(userName);
            textViewNav.setTextSize(20);
            menuItem.setTitle("Logout");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void processSearch(String s){
       FirebaseRecyclerOptions<Song> options = new FirebaseRecyclerOptions.Builder<Song>()
               .setQuery(FirebaseDatabase.getInstance().getReference().child("songs"),Song.class).build();

    }
}