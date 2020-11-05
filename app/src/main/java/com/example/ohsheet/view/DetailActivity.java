package com.example.ohsheet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ohsheet.R;

public class DetailActivity extends AppCompatActivity {
    private TextView txtTitle;
    private TextView txtWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        txtTitle = findViewById(R.id.txtTitle);
        txtWriter = findViewById(R.id.txtWriter);
        Intent intent = new Intent();
        String title = intent.getStringExtra("title");
        String writer = intent.getStringExtra("writer");
        txtTitle.setText(title);
        txtWriter.setText(writer);
    }
}