package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The activity which prompts the user to sign-in
 * using their credentials
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private FirebaseAuth fAuth;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        usernameField = findViewById(R.id.username_login);
        passwordField = findViewById(R.id.password_login);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // CHANGE THIS TO USE EMAILS

                // Ensure username is non-empty
                if (TextUtils.isEmpty(username)) {
                    usernameField.setError("Username is a required value.");
                    return;
                }

                // Ensure password is non-empty
                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Password is a required value.");
                    return;
                }

                /* (query email from provided username) */
                Database.getUserFromUsername(username)
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                // The username entered does exist
                                String email = (String) task.getResult().get("email");
                                fAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this,
                                                    "Login successful.",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                    "Error: " + task.getException().getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            }
                                        }
                                    });
                            } else if (!task.getResult().exists()) {
                                Toast.makeText(LoginActivity.this, "Username or password is incorrect.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error on login. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    /**
     * Redirects the user to the signup page.
     *
     * @param view the textview that was clicked on.
     */
    public void loginRedirectSignup(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}