package com.example.ohsheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ohsheet.adapter.CategoryAdapter;
import com.example.ohsheet.entity.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnNew;
    private GridView layout;
    private List<Category> list;
    private CategoryAdapter adapter;
    private FirebaseFirestore firestore;
//    private String imageString;
//
//    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.gridView);
        list = new ArrayList<>();
//        firestore = FirebaseFirestore.getInstance();
//        final CollectionReference reference = firestore.collection("`category");
//
//        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if(task.isSuccessful()){
//                    QuerySnapshot snapshots = task.getResult();
//
//                    for(QueryDocumentSnapshot doc:snapshots){
//                        Category category = new Category(
//                                doc.getId(),
//                                doc.get("categoryName").toString(),
//                                doc.get("categoryImg").toString()
//                        );
//
//                        list.add(category);
//
//                    }

//                }
//            }
//        });





//        storageReference = FirebaseStorage.getInstance().getReference().child("CategorySheet").child("aindie.jpg");
//        getImageData();


        list = new ArrayList<>();

        list.add(new Category(1, "aindie", R.drawable.aindie));
        list.add(new Category(2, "aindie", R.drawable.aindie));
        list.add(new Category(3, "aindie", R.drawable.aindie));
        list.add(new Category(4, "aindie", R.drawable.aindie));
        list.add(new Category(5, "aindie", R.drawable.aindie));
        list.add(new Category(6, "aindie", R.drawable.aindie));
        list.add(new Category(7, "aindie", R.drawable.aindie));
        list.add(new Category(8, "aindie", R.drawable.aindie));
        adapter = new CategoryAdapter(MainActivity.this, R.id.gridView, list);
        layout.setAdapter(adapter);



//        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });


//        btnNew = findViewById(R.id.btnNew);
//        btnNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),ListAllSheet.class);
//                startActivity(intent);
//            }
//        });

    }

//    private void getImageData() {
//        Toast.makeText(MainActivity.this, "hihihi", Toast.LENGTH_SHORT).show();
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_SHORT).show();
////                Log.i("img", uri.toString());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "hihi", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}