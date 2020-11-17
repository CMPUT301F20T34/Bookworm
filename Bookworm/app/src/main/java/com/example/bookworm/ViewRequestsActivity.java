package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewRequestsActivity extends AppCompatActivity {
    private Context context = this;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        recyclerView = findViewById(R.id.view_requests_recyclerview);

        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");

        Database.getRequestsForBook(isbn)
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(ViewRequestsActivity.this, "Could not get results. Please try again later.", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<Request> requestArrayList = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            requestArrayList.add(doc.toObject(Request.class));
                        }

                        ViewResultAdapter adapter = new ViewResultAdapter(requestArrayList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                }
            });
    }
}