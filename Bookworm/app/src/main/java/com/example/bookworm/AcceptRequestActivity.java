package com.example.bookworm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * List all of the requests on a book and let the user select which one to accept
 */
public class AcceptRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private ArrayList<Request> requestList;
    private Context context = this;
    int requestsLoaded = 0;
    String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        requestList = new ArrayList<Request>();
        //setup the recycler view and its adapter
        recyclerView = findViewById(R.id.request_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAdapter(this, requestList);
        recyclerView.setAdapter(adapter);
        Bundle b = getIntent().getExtras();
        String isbn = (String) b.get("isbn");
        Log.d(TAG, "Entered AcceptRequestActivity for isbn: " + isbn);

        //get requests on current book
        Database.queryCollection("requests", new String[]{"book.isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Log.d(TAG, "Adding request with user: " + doc.toObject(Request.class).getCreator().getUsername());
                            requestList.add(doc.toObject(Request.class));
                        }
                        requestsLoaded = 1;
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Could not load requests. Please try again.",
                                Toast.LENGTH_LONG).show();
                        requestsLoaded = 1;
                        finish();
                    }
                });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for (int i = 0; i < requestList.size(); i++){
                    Request acceptedRequest = requestList.get(i);
                    if (i == position){
                        acceptedRequest.setStatus("accepted");
                        Database.updateRequest(acceptedRequest);
                        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                                        scannedBook.setStatus("accepted");
                                        Database.writeBook(scannedBook);
                                    }
                                });
                    }
                    else{
                        acceptedRequest.setStatus("declined");
                        Database.updateRequest(acceptedRequest);
                    }
                }
                Toast.makeText(context,
                        "Successfully accepted request.",
                        Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                //do nothing
            }
        }));

    }
}
