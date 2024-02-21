package com.example.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editTextName, editTextEmail, editTextFeedback;
    private Button submitButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        submitButton = findViewById(R.id.submitButton);

        // Get the reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        try {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String feedback = editTextFeedback.getText().toString().trim();

            // Check if any field is empty
            if (name.isEmpty() || email.isEmpty() || feedback.isEmpty()) {
                Log.e(TAG, "One or more fields are empty.");
                return;
            }

            // Push data to Firebase database
            String key = databaseReference.push().getKey();
            if (key != null) {
                DatabaseReference feedbackRef = databaseReference.child(key);
                feedbackRef.child("Name").setValue(name);
                feedbackRef.child("Email").setValue(email);
                feedbackRef.child("Feedback").setValue(feedback);
            }

            // Clear input fields after submission
            editTextName.setText("");
            editTextEmail.setText("");
            editTextFeedback.setText("");
        } catch (Exception e) {
            Log.e(TAG, "Error submitting feedback: " + e.getMessage());
        }
    }
}
