package com.example.bookworm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * The main activity of the program which delegates all tasks,
 * other than signing-in or registering an account.
 */
public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";
    private Button myBooklistButton;
    private Button myProfileButton;
    private Button mySearchButton;
    private Button myBorrowerInfoButton;
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        final EditText mySearchTerm = findViewById(R.id.keywordSearchBar);

        // Allow realtime updates for database
        Database.createListener();

        // If a user is not registered, redirect them
        // to the signup screen.
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }

        // Update the registration token
        Database.updateUserRegistrationToken();

        // Create listeners for the buttons
        myBooklistButton = findViewById(R.id.booklist_button);
        myBooklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(myBooklistButton.getContext(), OwnerBooklistActivity.class));
            }
        });

        myProfileButton = findViewById(R.id.profile_button);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        mySearchButton = findViewById(R.id.search_button);
        mySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox titleCheckbox = findViewById(R.id.titleCheckbox);
                CheckBox descCheckbox = findViewById(R.id.descCheckbox);
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                if (titleCheckbox.isChecked()){
                    intent.putExtra("type", "title");
                }
                else if (descCheckbox.isChecked()){
                    intent.putExtra("type", "description");
                }
                if (titleCheckbox.isChecked() ^ descCheckbox.isChecked()){
                    intent.putExtra("searchTerm", mySearchTerm.getText().toString());
                    startActivity(intent);
                }
            }
        });
        myBorrowerInfoButton = findViewById(R.id.borrow_info_button);
        myBorrowerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BorrowerMainActivity.class));
            }
        });
//        startActivity(new Intent(getApplicationContext(), BorrowerMapActivity.class));
    }
}
