package com.example.vulnerableapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://02d1-49-207-224-145.ngrok-free.app"; // Replace with your API base URL
    private TextView textViewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResponse = findViewById(R.id.textViewResponse);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        findViewById(R.id.buttonBufferOverflow).setOnClickListener(v ->
                new ApiRequestTask().execute("/buffer", "data=A"));

        findViewById(R.id.buttonCommandInjection).setOnClickListener(v ->
                new ApiRequestTask().execute("/cmd", "cmd=echo hello"));

        findViewById(R.id.buttonXSS).setOnClickListener(v ->
                new ApiRequestTask().execute("/xss", "input=<script>alert('XSS')</script>"));

        findViewById(R.id.buttonIntegerOverflow).setOnClickListener(v ->
                new ApiRequestTask().execute("/int-overflow", "number=2147483640"));

        findViewById(R.id.buttonJsonDepthOverflow).setOnClickListener(v ->
                new ApiRequestTask().execute("/json-depth", "{\"key\":\"value\"}"));

        findViewById(R.id.buttonLdapInjection).setOnClickListener(v ->
                new ApiRequestTask().execute("/ldap", "user=testuser"));

        findViewById(R.id.buttonRegexDos).setOnClickListener(v ->
                new ApiRequestTask().execute("/regex", "pattern=(a+)+"));

        findViewById(R.id.buttonSqlInjection).setOnClickListener(v ->
                new ApiRequestTask().execute("/sql", "query=admin' OR '1'='1"));

        findViewById(R.id.buttonStringValidation).setOnClickListener(v ->
                new ApiRequestTask().execute("/string-validate", "data=<script>"));

        findViewById(R.id.buttonXmlExternalEntity).setOnClickListener(v ->
                new ApiRequestTask().execute("/xml", "<!DOCTYPE foo [<!ENTITY xxe SYSTEM 'file:///etc/passwd'>]><foo>&xxe;</foo>"));

        findViewById(R.id.buttonCrossSiteTracing).setOnClickListener(v ->
                new ApiRequestTask().execute("/trace", ""));

        findViewById(R.id.buttonNonHttpsLinks).setOnClickListener(v ->
                new ApiRequestTask().execute("/non-https", ""));

        findViewById(R.id.buttonHostHeaderInjection).setOnClickListener(v ->
                new ApiRequestTask().execute("/host-header", ""));

        findViewById(R.id.buttonOpensslCcs).setOnClickListener(v ->
                new ApiRequestTask().execute("/openssl-ccs", ""));

        findViewById(R.id.buttonTlsRenegotiation).setOnClickListener(v ->
                new ApiRequestTask().execute("/tls-renegotiation", ""));

        findViewById(R.id.buttonTlsDowngrade).setOnClickListener(v ->
                new ApiRequestTask().execute("/tls-downgrade", ""));

        findViewById(R.id.buttonTlsRobot).setOnClickListener(v ->
                new ApiRequestTask().execute("/tls-robot", ""));

        findViewById(R.id.buttonTlsCrime).setOnClickListener(v ->
                new ApiRequestTask().execute("/tls-crime", ""));

        findViewById(R.id.buttonHeartbleed).setOnClickListener(v ->
                new ApiRequestTask().execute("/heartbleed", ""));

        findViewById(R.id.buttonGeneralServer).setOnClickListener(v ->
                new ApiRequestTask().execute("/general-server", ""));
    }

    private class ApiRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String endpoint = params[0];
            String postData = params[1];

            try {
                URL url = new URL(BASE_URL + endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResponse.setText(result);
        }
    }
}
