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

import com.example.bookworm.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Handles all actions relating to registering an account, including validating the form.
 */
public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText usernameField;
    private EditText password1Field;
    private EditText password2Field;
    private EditText emailField;
    private EditText phoneNumberField;
    private FirebaseAuth fAuth;
    private Button register;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.username_signup);
        password1Field = findViewById(R.id.password1_signup);
        password2Field = findViewById(R.id.password2_signup);
        emailField = findViewById(R.id.email_address_signup);
        phoneNumberField = findViewById(R.id.phone_number_signup);
        register = findViewById(R.id.register_button);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username   = usernameField.getText().toString().trim();
                final String password1  = password1Field.getText().toString().trim();
                final String password2  = password2Field.getText().toString().trim();
                final String email  = emailField.getText().toString().trim();
                final String phoneNumber = phoneNumberField.getText().toString().trim();

                // Ensure username is non-empty
                if (TextUtils.isEmpty(username)) {
                    usernameField.setError("Username is a required value.");
                    return;
                }

                // Ensure email is non-empty
                if (TextUtils.isEmpty(email)) {
                    emailField.setError("Email is a required value.");
                    return;
                }

                if (!Util.validateEmail(email)) {
                    emailField.setError("Email is incorrectly formatted");
                    return;
                }

                // Ensure password is non-empty
                if (TextUtils.isEmpty(password1)) {
                    password1Field.setError("Password is a required value.");
                    return;
                }

                // Ensure passwords match
                if (!TextUtils.equals(password1, password2)) {
                    password1Field.setError("Passwords must match.");
                    return;
                }

                // Validate phone number is correct
                if (!Util.validatePhoneNumber(phoneNumber)) {
                    phoneNumberField.setError("Phone number is incorrectly formatted");
                    return;
                }

                // Phone number is allowed to be empty

                /* Check if the username is already taken */
                Database.getUserFromUsername(username)
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().exists()) {
                                // Username does not already exist
                                fAuth.createUserWithEmailAndPassword(email, password1)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            // Successfully created user, redirect to main activity
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this,
                                                    "User has been created",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                                Database.createUser(username, phoneNumber, email);
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            } else {

                                                // Notify of failed user creation
                                                Toast.makeText(SignUpActivity.this,
                                                    "Error: " + task.getException().getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            }
                                        }
                                    });
                            } else if (task.getResult().exists()) {
                                /* Username is taken */
                                Toast.makeText(SignUpActivity.this,
                                    "Error: Username already exists",
                                    Toast.LENGTH_LONG)
                                    .show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error on signup. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    /**
     * Redirects the user to the login screen
     * @param view the clicked text
     */
    public void signupRedirectLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}