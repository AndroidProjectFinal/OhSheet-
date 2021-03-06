package com.example.ohsheet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ohsheet.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView txtTitle;
    private TextView txtWriter;
    private TextView txtLink;
    private ImageView imgView;
    private ArrayAdapter adapter;
    private List listItem;
    private Spinner spinner;
    private String linkYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTitle = findViewById(R.id.txtTitle);
        txtWriter = findViewById(R.id.txtWriter);
        txtLink = findViewById(R.id.txtLinkMusic);
        imgView = findViewById(R.id.imgSheet);

        listItem = new ArrayList();
        listItem.add("Sheet");
        listItem.add("Chord");
        spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,listItem);
        spinner.setAdapter(adapter);

        Intent intent = new Intent();
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("title");
        String link = bundle.getString("link");
        String writer = bundle.getString("writer");
        final String sheet = bundle.getString("sheet");
        final String content = bundle.getString("content");
        linkYou = link;
        txtTitle.setText(message);
        txtWriter.setText(writer);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Sheet")){
                    Glide.with(getApplicationContext())
                            .load(sheet)
                            .into(imgView);
                }else{
                    Glide.with(getApplicationContext())
                            .load(content)
                            .into(imgView);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),YoutubeActivity.class);
        intent.putExtra("linkVideo", linkYou);
        startActivity(intent);
    }
}