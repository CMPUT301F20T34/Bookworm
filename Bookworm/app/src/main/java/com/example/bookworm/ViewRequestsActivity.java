package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
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
    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        recyclerView = findViewById(R.id.view_requests_recyclerview);

        Intent intent = getIntent();
        this.isbn = intent.getStringExtra("isbn");

        getRequests();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null){
                    String username = ((TextView) view.findViewById(R.id.view_request_result_username)).getText().toString();
                    Intent intent = new Intent(recyclerView.getContext(), AcceptDeclineRequestActivity.class);
                    intent.putExtra("username", username);
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
     * Refreshes the list of requests after either accepting or declining
     * a request
     */
    @Override
    protected void onResume() {
        super.onResume();
        getRequests();
    }

    /**
     * Performs the call to the database to get the list of requests
     * for the selected book.
     */
    private void getRequests() {
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

                        ViewRequestsAdapter adapter = new ViewRequestsAdapter(requestArrayList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                }
            });
    }
}