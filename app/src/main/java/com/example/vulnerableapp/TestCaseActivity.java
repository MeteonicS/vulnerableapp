package com.example.vulnerableapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestCaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_case);

        // Find the TextView by its ID
        TextView testCaseTextView = findViewById(R.id.test_case_text);

        // Get the test case name from the Intent
        String testCaseName = getIntent().getStringExtra("test_case_name");

        // Set the test case name to the TextView
        if (testCaseName != null) {
            testCaseTextView.setText(testCaseName);
        }
    }
}
