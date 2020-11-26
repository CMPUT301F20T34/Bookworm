package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

/**
 * Lists all the books that a user currently owns,
 * allows for scanning, adding, or editing books.
 */
public class OwnerBooklistActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private ArrayList<Book> booklist;
    private RecyclerView recyclerView;
    private BooklistAdapter bookListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context = this;
    Button addBookButton;
    RadioGroup radioGroup;
    RadioButton availableBtn, requestedBtn, acceptedBtn, borrowedBtn, defaultBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booklist);

        fAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        queryBook(-1);

        availableBtn = (RadioButton) findViewById(R.id.radio_available);
        requestedBtn = (RadioButton) findViewById(R.id.radio_requested);
        acceptedBtn = (RadioButton) findViewById(R.id.radio_accepted);
        borrowedBtn = (RadioButton) findViewById(R.id.radio_borrowed);
        defaultBtn = (RadioButton) findViewById(R.id.radio_default);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        //filter the list according to the radio button pressed
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                queryBook(checkedId);
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

    /**
     * Updates the booklist when activity has returned from elsewhere.
     */
    @Override
    protected void onResume() {
        super.onResume();
        queryBook(-1);
    }

    /**
     * Queries books with the required information for the currently
     * signed-in user according to the input id.
     * @param id
     */
    private void queryBook(int id) {
        String[] fields = {"ownerId"};
        String[] values = {fAuth.getCurrentUser().getUid()};
        Database.queryCollection("books", fields, values).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    booklist = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Book ownerBook = document.toObject(Book.class);

                        switch (id){
                            case R.id.radio_available:
                                if (ownerBook.getStatus().equals("available"))
                                    booklist.add(ownerBook);
                                break;
                            case R.id.radio_requested:
                                if (ownerBook.getStatus().equals("requested"))
                                    booklist.add(ownerBook);
                                break;
                            case R.id.radio_accepted:
                                if (ownerBook.getStatus().equals("accepted"))
                                    booklist.add(ownerBook);
                                break;
                            case R.id.radio_borrowed:
                                if (ownerBook.getStatus().equals("borrowed"))
                                    booklist.add(ownerBook);
                                break;
                            case R.id.radio_default:
                                booklist.add(ownerBook);
                                break;
                            case -1:
                                booklist.add(ownerBook);
                                break;
                        }
                    }
                    bookListAdapter = new BooklistAdapter(context, booklist);
                    recyclerView.setAdapter(bookListAdapter);
                }
            }
        });
    }
}