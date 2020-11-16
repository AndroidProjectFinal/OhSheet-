package com.example.ohsheet.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityCreateGenre extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNameGenre;
    private Button btnAddImage;
    private Button btnSave;
    private ImageView imageView;

    private Uri imageUri;

    private StorageReference storageRef;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_genre);


        btnAddImage = findViewById(R.id.btnAddImage);
        btnSave = findViewById(R.id.btnSave);
        imageView = findViewById(R.id.imageView);
        etNameGenre = findViewById(R.id.etNameGenre);

        storageRef = FirebaseStorage.getInstance().getReference("CategorySheet");
        firestore = FirebaseFirestore.getInstance();

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        final Bundle bundle = getIntent().getExtras();
        if(bundle.getString("updateGenre").equals("Update")){
            etNameGenre.setText(bundle.getString("genreName"));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getString("updateGenre").equals("Create")){
                    uploadFile();
                    Toast.makeText(ActivityCreateGenre.this, "Create Successfully!", Toast.LENGTH_SHORT).show();
                }
                if(bundle.getString("updateGenre").equals("Update")){
                    updateFile(bundle.getString("genreName"));

                }
            }
        });
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


    private void updateFile(String genreName) {
        final CollectionReference reference = firestore.collection("genre");
        reference.whereEqualTo("genreName", genreName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final QueryDocumentSnapshot doc: task.getResult()){
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
                                        Genre genre = new Genre(etNameGenre.getText().toString().trim(),
                                                downloadUri.toString());
                                        reference.document(doc.getId()).set(genre, SetOptions.merge());
                                        Toast.makeText(ActivityCreateGenre.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else
                                    {
                                        Log.i("TAG", "this is msg");
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(ActivityCreateGenre.this, "Please choose image!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    private void uploadFile(){
        final CollectionReference reference = firestore.collection("genre");
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
                        Genre genre = new Genre(etNameGenre.getText().toString().trim(),
                                downloadUri.toString());
                        reference.add(genre);
                    } else
                    {
                        Log.i("TAG", "this is msg");
                    }
                    Toast.makeText(ActivityCreateGenre.this, "Add Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else{
            Toast.makeText(ActivityCreateGenre.this, "Please choose image!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}