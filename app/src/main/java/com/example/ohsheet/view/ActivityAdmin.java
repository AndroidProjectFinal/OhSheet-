package com.example.ohsheet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ohsheet.R;

public class ActivityAdmin extends AppCompatActivity {

    private Button btnCreateSong;
    private Button btnCreateGenre;
    private Button btnListSong;
    private Button btnListGenre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnCreateSong = findViewById(R.id.btnCreateSong);
        btnCreateGenre = findViewById(R.id.btnCreateGenre);
        btnListSong = findViewById(R.id.btnListSong);
        btnListGenre = findViewById(R.id.btnListGenre);

        btnCreateSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityCreateSong.class);
                startActivity(intent);
            }
        });
        btnCreateGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityCreateGenre.class);
                startActivity(intent);
            }
        });
        btnListSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListSong.class);
                startActivity(intent);
            }
        });
        btnListGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListGenre.class);
                startActivity(intent);
            }
        });




    }
}