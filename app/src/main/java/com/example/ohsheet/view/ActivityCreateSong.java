package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;
import com.example.ohsheet.entity.Song;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActivityCreateSong extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE = 1;
    private EditText etTitleSong;
    private EditText etWriter;
    private Button btnAddImage;
    private Spinner spLev;
    private EditText etLinkMusic;
    private Button btnAddContent;
    private Button btnSave;
    private ImageView imageView;
    private Spinner spGenre;
    private Uri imageUri;
    private TextView txtAlert;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private ProgressDialog progressDialog;

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
        btnAddContent = findViewById(R.id.btnAddContent);
        btnSave = findViewById(R.id.btnSave);
        imageView = findViewById(R.id.imageView);
        spGenre = findViewById(R.id.spGenre);
        txtAlert = findViewById(R.id.txtAlert);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image uploading please wait.....");


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

        final CollectionReference reference = firestore.collection("genre");

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();
                    List<Genre> list = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : snapshot){
                        Genre  genre = new Genre();
                        genre.setGenreName(doc.get("genreName").toString());

                        list.add(genre);

                    }
                    ArrayAdapter<Genre> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
                    spGenre.setAdapter(adapter);
                }
            }
        });

        btnAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });


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
        }else if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData()!=null && data.getClipData() != null){


            int countClipData = data.getClipData().getItemCount();
            int currentImageSelect = 0;

            while (currentImageSelect<countClipData){

                imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                ImageList.add(imageUri);

                currentImageSelect = currentImageSelect+1;

            }

            txtAlert.setVisibility(View.VISIBLE);
            txtAlert.setText("You have selected" +ImageList.size()+"images");
            btnAddContent.setVisibility(View.GONE);



        }



    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        progressDialog.show();
        StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("");


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
                        Song song = new Song(
                                etTitleSong.getText().toString().trim(),
                                etWriter.getText().toString().trim(),
                                "Sheet day",
                                Integer.parseInt(spLev.getSelectedItem().toString()),
                                "ngay day",
                                etLinkMusic.getText().toString().trim());
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