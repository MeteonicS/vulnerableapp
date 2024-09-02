package com.example.vulnerableapp;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class XSSExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xss_example);

        WebView webView = findViewById(R.id.webview);
        String xssPayload = "<script>alert('XSS')</script>";
        webView.loadData(xssPayload, "text/html", "UTF-8");
    }
}
