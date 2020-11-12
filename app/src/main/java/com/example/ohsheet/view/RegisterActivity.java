package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUserName;
    private EditText etPassWord;
    private EditText etcfPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegisterClick);
        etUserName = findViewById(R.id.etUserNameRegister);
        etPassWord = findViewById(R.id.etPassWordRegister);
        etcfPassWord = findViewById(R.id.etCfPassWord);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount();


            }
        });
    }
    private void createAccount(){
        String userName = etUserName.getText().toString().trim();
        String passWord = etPassWord.getText().toString().trim();
        String cfPassWord = etcfPassWord.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            etUserName.setError("UserName is not empty! ");
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            etPassWord.setError("PassWord is not empty! ");
            return;
        }
        if(passWord.length() < 6){
            etPassWord.setError("PassWord must be >= 6 characters ! ");
            return;
        }
        if(cfPassWord.equals(passWord)==false){
            etcfPassWord.setError("CFPassword must be true Password!");
            return;
        }
        validateUserName(userName,passWord);

    }
    private void validateUserName(final String userName, final String passWord) {
        final FirebaseFirestore reference;
        reference = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = reference.collection("users");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    for(QueryDocumentSnapshot docs : snapshots){
                        if(docs.get("username").toString().equals(userName)){
                            etUserName.setError("UserName is exist please choose other name!");
                            return;
                        }
                    }
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username", userName);
                    hashMap.put("password", passWord);
                    collectionReference.add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Register Fail!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        finish();
    }
}

