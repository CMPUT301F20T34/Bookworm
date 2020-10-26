package com.example.bookworm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class OwnerBooklistActivity extends AppCompatActivity {

    private ArrayList<Book> booklist;
    private RecyclerView recyclerView;
    private BooklistAdapter bookListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Button addBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booklist);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        booklist = new ArrayList<Book>();
        booklist.add(new Book("1", "2", "3"));
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
    }
}