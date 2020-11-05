package com.example.bookworm;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListAccepted extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Book> booklist;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_lists);
        booklist = new ArrayList<Book>();
        //setup the recycler view and its adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,booklist);
        recyclerView.setAdapter(adapter);
        //get books with accepted requests of the current users
        Database.getAcceptedBooks()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //convert QuerySnapshot to a request object
                            Request request = doc.toObject(Request.class);
                            //add the accepted book to the list
                            booklist.add(request.getBook());
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Could not load accepted books. Please try again.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
