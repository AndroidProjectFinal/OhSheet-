package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;
import com.example.ohsheet.entity.Song;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityCreateSong extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etTitleSong;
    private EditText etWriter;
    private Button btnAddImage;
    private Spinner spLev;
    private EditText etLinkMusic;
    private EditText etContent;
    private Button btnSave;
    private ImageView imageView;

    private Uri imageUri;

    private StorageReference storageRef;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);
        etTitleSong = findViewById(R.id.etTitleSong);
        etWriter = findViewById(R.id.etWriter);
        btnAddImage = findViewById(R.id.btnAddImage);
        spLev = findViewById(R.id.spLev);
        etLinkMusic = findViewById(R.id.etLinkMusic);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);
        imageView = findViewById(R.id.imageView);

        storageRef = FirebaseStorage.getInstance().getReference("Sheet");
        firestore = FirebaseFirestore.getInstance();


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spLev.setAdapter(arrayAdapter);


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });


    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData()!=null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(imageView);
            imageView.setImageURI(imageUri);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        final CollectionReference reference = firestore.collection("songs");
        if (imageUri != null)
        {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." +getFileExtension(imageUri));
            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        Log.e("TAG", "then: " + downloadUri.toString());
                        Song song = new Song(etTitleSong.getText().toString().trim(),etWriter.getText().toString().trim(),
                                "Sheet day",Integer.parseInt(spLev.getSelectedItem().toString()),
                                "ngay day",etLinkMusic.getText().toString().trim());
                        reference.add(song);
                    } else
                    {
                        Log.i("TAG", "tai mat loz");
                    }
                }
            });
        }
    }


}