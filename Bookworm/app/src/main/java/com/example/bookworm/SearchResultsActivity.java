package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Shows the results of a search, whether it was by title or keyword
 */
public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.OnBookListener {
    final private ArrayList<Book> books = new ArrayList<>();
    final private Context context = this;
    final private SearchResultsAdapter.OnBookListener onBookListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("searchTerm");
        final RecyclerView searchResults = findViewById(R.id.search_results_recyclerview);
        TextView displayTerm = findViewById(R.id.search_results_display);
        displayTerm.setText(searchTerm);

        // Get the results for each book, display in the user
        Task<QuerySnapshot> searchTask;
        if (intent.getStringExtra("type").equals("title")){
            searchTask = Database.searchBooks(searchTerm);
        }
        else{
            searchTask = Database.bookKeywordSearch(new String[]{"available", "requested"}, searchTerm);
        }
        searchTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        books.add(doc.toObject(Book.class));
                    }
                    SearchResultsAdapter adapter = new SearchResultsAdapter(context, books, onBookListener);
                    searchResults.setAdapter(adapter);
                    searchResults.setLayoutManager(new LinearLayoutManager(context));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                }
            });
    }

    /**
     * Views the book in a new activity when a search result is tapped on
     * @param position the position of the tap in the recyclerView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBookClick(int position) {
        Book selectedBook = this.books.get(position);
        Intent intent = new Intent(this, ViewBookActivity.class);
        intent.putExtra("title", selectedBook.getTitle());
        intent.putExtra("author", selectedBook.getAuthor());
        intent.putExtra("owner", selectedBook.getOwner());
        intent.putExtra("status", selectedBook.getStatus());
        intent.putExtra("description", selectedBook.descriptionAsString());
        intent.putExtra("isbn", selectedBook.getIsbn());
        startActivity(intent);
    }
}