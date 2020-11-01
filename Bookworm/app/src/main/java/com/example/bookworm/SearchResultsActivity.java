package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("searchTerm");
        TextView displayTerm = findViewById(R.id.search_results_display);
        displayTerm.setText(searchTerm);
        final ArrayList<Book> books = new ArrayList<>();
        final RecyclerView searchResults = findViewById(R.id.search_results_recyclerview);
        final Context context = this;

        Database.searchBooks(searchTerm)
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        books.add(new Book(
                            (String) doc.get("title"),
                            (String) doc.get("author"),
                            (String) doc.get("owner"),
                            (String) doc.get("status")
                        ));
                    }
                    SearchResultsAdapter adapter = new SearchResultsAdapter(context, books);
                    searchResults.setAdapter(adapter);
                    searchResults.setLayoutManager(new LinearLayoutManager(context));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }
}