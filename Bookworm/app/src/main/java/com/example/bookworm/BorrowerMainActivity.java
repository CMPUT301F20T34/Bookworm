package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BorrowerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);
    }
    public void listBorrowing(View view) {
        Intent intent = new Intent(this, ListBorrowing.class);
        startActivity(intent);
    }
    public void listRequested(View view){
        Intent intent = new Intent(this, ListRequested.class);
        startActivity(intent);
    }
    public void listAccepted(View view){
        Intent intent = new Intent(this, ListRequested.class);
        startActivity(intent);
    }
}