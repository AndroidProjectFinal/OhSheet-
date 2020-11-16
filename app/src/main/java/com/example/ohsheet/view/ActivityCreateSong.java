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
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ActivityCreateSong extends AppCompatActivity {
    public static int REQUEST_CODE_SONG = 300;
    public static int RESULT_CODE_SONG = 400;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE = 2;
    private int upload_count =0;
    private EditText etTitleSong,etWriter,etLinkMusic;
    private Button btnAddImage,btnAddContent,btnSave;
    private Spinner spLev,spGenre;
    private ImageView imageView,imageViewContent;
    private Uri imageUri, contentUri;
    private TextView txtAlert;
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
        imageViewContent = findViewById(R.id.imageViewContent);
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList);
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

        final Bundle bundle = getIntent().getExtras();
        if(bundle.getString("updateSong").equals("Update")){
            etTitleSong.setText(bundle.getString("nameSong"));
            etWriter.setText(bundle.getString("writerSong"));
            spLev.setSelection(Integer.parseInt(bundle.getString("levelSong")));
            spGenre.setSelection(Integer.parseInt(bundle.getString("genreSong")));
            etLinkMusic.setText(bundle.getString("linkSong"));
        }

        btnAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getString("updateSong").equals("Create")){
                    uploadFile();
                }
                if(bundle.getString("updateSong").equals("Update")){
                    updateFile(bundle.getString("nameSong"));

                }
            }
        });
    }



    private void updateFile(final String nameSong) {
        progressDialog.show();
        readData(new MyCallback() {
            @Override
            public void onCallback(String value) {
                final String content = value;
                Calendar c = Calendar.getInstance();
                final  String date = c.get(Calendar.HOUR) +":"+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) +"----"+ c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);
                final CollectionReference reference = firestore.collection("songs");
                reference.whereEqualTo("title", nameSong).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                                Song song = new Song(
                                                        etTitleSong.getText().toString().trim(),
                                                        etWriter.getText().toString().trim(),
                                                        downloadUri.toString(),
                                                        content,
                                                        Integer.parseInt(spLev.getSelectedItem().toString()),
                                                        spGenre.getSelectedItem().toString(),
                                                        "Tungdt",
                                                        date,
                                                        etLinkMusic.getText().toString().trim());
                                                reference.document(doc.getId()).set(song, SetOptions.merge());
                                                Toast.makeText(ActivityCreateSong.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else
                                            {
                                                Log.i("TAG", "Error");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

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
                && data != null && data.getData()!=null) {

            contentUri = data.getData();
            Picasso.get().load(contentUri).into(imageViewContent);
            imageViewContent.setImageURI(contentUri);


//            Picasso.get().load(contentUri).into(imageView);
//            imageView.setImageURI(imageUri);
//            int countClipData = data.getClipData().getItemCount();
//            int currentImageSelect = 0;
//
//            while (currentImageSelect<countClipData)
//            {
//                imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
//                ImageList.add(imageUri);
//                currentImageSelect++;
//            }
//            txtAlert.setVisibility(View.VISIBLE);
//            txtAlert.setText("You have selected" +ImageList.size()+"images");
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private ArrayList<String> stringUrl = new ArrayList<>();

    private void uploadFile(){

        progressDialog.show();
        readData(new MyCallback() {
            @Override
            public void onCallback(String value) {
                final String content = value;
                Calendar c = Calendar.getInstance();
                final  String date = c.get(Calendar.HOUR) +":"+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) +"----"+ c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);
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
                                        downloadUri.toString(),
                                        content,
                                        Integer.parseInt(spLev.getSelectedItem().toString()),
                                        spGenre.getSelectedItem().toString(),
                                        "Tungdt",
                                        date,
                                        etLinkMusic.getText().toString().trim());
                                reference.add(song);
                            } else
                            {
                                Log.i("TAG", "Error");
                            }
                        }
                    });
                }
            }
        });
//        StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("Content");
//        for(upload_count=0;upload_count<ImageList.size();upload_count++){
//            Uri IndividualImage = ImageList.get(upload_count);
//            final StorageReference imageName = imageFolder.child("Image"+IndividualImage.getLastPathSegment());
//            imageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String url = String.valueOf(uri);
//                        stringUrl.add(url);
//                        StoreLink(url);
//                    }
//                });
//            }
//        });
//        }





    }

//    private void StoreLink(String url) {
//        CollectionReference reference = firestore.collection("Content");
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("Imglink",url);
//        reference.add(hashMap);
//        progressDialog.dismiss();
//        txtAlert.setText("Successfully");
//}
    public interface MyCallback {
        void onCallback(String value);
    }
    public void readData(final MyCallback myCallback) {
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
                        myCallback.onCallback(downloadUri.toString());
                    } else
                    {
                        Log.i("TAG", "Error");
                    }
                }
            });
        }
    }

}