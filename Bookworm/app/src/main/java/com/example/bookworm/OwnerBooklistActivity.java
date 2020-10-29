package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OwnerBooklistActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private ArrayList<Book> booklist;
    private RecyclerView recyclerView;
    private BooklistAdapter bookListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Button addBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booklist);

        fAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Book> temp = Database.getLibrary().getBooks();
        booklist = new ArrayList<Book>();
        for (Book book : temp) {
            if (book.getOwnerId().equals(fAuth.getCurrentUser().getUid())) {
                booklist.add(book);
            }
        }
        bookListAdapter = new BooklistAdapter(booklist);
        recyclerView.setAdapter(bookListAdapter);

        addBookButton = findViewById(R.id.button2);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addBookButton.getContext(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null){
                    String title = ((TextView) view.findViewById(R.id.textView2)).getText().toString();
                    String author = ((TextView) view.findViewById(R.id.textView3)).getText().toString();
                    String isbn = ((TextView) view.findViewById(R.id.textView4)).getText().toString();
                    Book selectedBook = null;
                    for (Book book : booklist) {
                        if (book.getTitle().equals(title) && book.getAuthor().equals(author) && book.getIsbn().equals(isbn)) {
                            selectedBook = book;
                        }
                    }
                    if (selectedBook != null) {
                        Intent intent = new Intent(recyclerView.getContext(), EditBookActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("author", author);
                        intent.putExtra("isbn", isbn);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}