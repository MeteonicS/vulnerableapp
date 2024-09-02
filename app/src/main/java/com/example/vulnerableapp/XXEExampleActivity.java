package com.example.vulnerableapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayInputStream; // Import ByteArrayInputStream
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;

public class XXEExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xxe_example);

        String xmlInput = "<?xml version=\"1.0\"?><!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///android_asset/secret.txt\">]><foo>&xxe;</foo>";
        InputStream inputStream = new ByteArrayInputStream(xmlInput.getBytes());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
            factory.newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
