package com.example.ohsheet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("title");

        String writer = bundle.getString("writer");
        txtTitle.setText(message);
        txtWriter.setText(writer);
    }
}