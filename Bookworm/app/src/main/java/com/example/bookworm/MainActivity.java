package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database


        Intent intent = new Intent(this, OwnerBooklistActivity.class);
        startActivity(intent);
    }
}