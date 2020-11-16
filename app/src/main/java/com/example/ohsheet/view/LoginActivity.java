package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;
    private EditText etUserName;
    private EditText etPassWord;
    private TextView txtAdmin;
    private TextView txtUser;
    private CheckBox cbRemember;
    private static final String SHARE_PREF_NAME = "data";
    private static final String KEY_NAME = "key_username";
    private static final String KEY_PASS = "key_password";
    String userName;
    String passWord;
    boolean checkOnOf;

    private String parentNameDb="users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
        txtAdmin = findViewById(R.id.textViewAdmin);
        txtUser = findViewById(R.id.textViewUser);
        cbRemember = findViewById(R.id.cbRemember);

        displayRemember();
        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setText("Admin login");
                txtAdmin.setVisibility(View.INVISIBLE);
                txtUser.setVisibility(View.VISIBLE);
                cbRemember.setVisibility(View.INVISIBLE);
                parentNameDb = "admins";
            }
        });

        txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setText("Login");
                txtAdmin.setVisibility(View.VISIBLE);
                txtUser.setVisibility(View.INVISIBLE);
                cbRemember.setVisibility(View.VISIBLE);
                parentNameDb = "users";
            }
        });

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    remeberInfo();


                }else if(!compoundButton.isChecked()) {
                    remeberInfo();

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();

            }
        });

    }

    private void LoginUser(){
        userName = etUserName.getText().toString().trim();
        passWord = etPassWord.getText().toString().trim();
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

        AllowAccessToAccount(userName,passWord);
    }
    private void AllowAccessToAccount(final String userName, final String passWord){
        final FirebaseFirestore reference;
        reference = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = reference.collection(parentNameDb);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    for(QueryDocumentSnapshot docs : snapshots){
                        if(docs.get("username").toString().equals(userName) && docs.get("password").toString().equals(passWord)){

                            if(parentNameDb.equals("users")){
                                Intent intent = new Intent();
                                intent.putExtra("userName", userName);
                                setResult(MainActivity.RESULT_CODE, intent);
                            }
                            if(parentNameDb.equals("admins")){
                                Intent intent = new Intent(LoginActivity.this, ActivityAdmin.class);

                                startActivity(intent);
                            }
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });
    }
    private void displayRemember(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        userName = sharedPreferences.getString("username", "");
        passWord = sharedPreferences.getString("password", "");
        checkOnOf = sharedPreferences.getBoolean("checkbox",false);
        cbRemember.setChecked(checkOnOf);
        if(cbRemember.isChecked()==true)
        {
            if(userName!= null && passWord!= null){
                etUserName.setText(userName);
                Log.i("userName", userName);
                etPassWord.setText(passWord);
            }
        } else
        {
            etPassWord.setText("");
            etUserName.setText("");
        }
    }

    private void remeberInfo(){
        SharedPreferences preferences = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userName = etUserName.getText().toString();
        passWord = etPassWord.getText().toString();

        editor.putString("username", String.valueOf(userName));
        editor.putString("password", String.valueOf(passWord));
        editor.putBoolean("checkbox", cbRemember.isChecked());

        editor.apply();
    }
}