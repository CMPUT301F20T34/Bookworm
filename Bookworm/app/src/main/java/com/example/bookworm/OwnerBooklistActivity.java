package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnerBooklistActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private ArrayList<Book> booklist;
    private RecyclerView recyclerView;
    private BooklistAdapter bookListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context = this;
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

        String[] fields = {"ownerId"};
        String[] values = {fAuth.getCurrentUser().getUid()};
        Database.queryCollection("books", fields, values).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                booklist = new ArrayList<Book>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    booklist.add(document.toObject(Book.class));
                }
                bookListAdapter = new BooklistAdapter(context, booklist);
                recyclerView.setAdapter(bookListAdapter);
            }
            }
        });

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
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null){
                    String isbn = ((TextView) view.findViewById(R.id.phone_profile)).getText().toString();
                    Intent intent = new Intent(recyclerView.getContext(), EditBookActivity.class);
                    intent.putExtra("isbn", isbn);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}