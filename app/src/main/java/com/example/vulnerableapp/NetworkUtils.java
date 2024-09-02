package com.example.vulnerableapp;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String API_URL = "https://4f74-49-207-224-145.ngrok-free.app/api/test";

    public static void simulateApiCall(String testCaseName) {
        // Disable SSL certificate checking (for testing purposes only)
        disableSSLCertificateChecking();

        String requestData;

        // Construct the request data based on the test case name
        requestData = getRequestDataForTestCase(testCaseName);

        // Perform HTTP request
        performHttpRequest(API_URL, requestData);
    }

    private static void disableSSLCertificateChecking() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) (hostname, session) -> true);
        } catch (Exception e) {
            Log.e(TAG, "Failed to disable SSL certificate checking", e);
        }
    }

    private static String getRequestDataForTestCase(String testCaseName) {
        switch (testCaseName) {
            case "Buffer Overflow vulnerabilities in HTTP Requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"input\": \"" + generateLargeString() + "\" }";
            case "Command Injection vulnerabilities in HTTP Requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"command\": \"rm -rf /\" }";
            case "Cross Site Scripting (XSS) vulnerabilities in HTTP Body":
                return "{ \"test_case\": \"" + testCaseName + "\", \"script\": \"<script>alert('XSS')</script>\" }";
            case "Integer Overflow vulnerability in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"number\": \"2147483648\" }";
            case "JSON Depth Overflow in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"json\": " + generateDeepJson(20) + " }";
            case "LDAP Injection Vulnerabilities in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"search\": \"*)(|(user=*))\" }";
            case "Regex DoS Vulnerabilities in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"input\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaaa!\" }";
            case "SQL Injection Vulnerabilities in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"query\": \"SELECT * FROM users WHERE username = 'admin' OR 1=1 --'\" }";
            case "String Validation Vulnerabilities in HTTP requests":
                return "{ \"test_case\": \"" + testCaseName + "\", \"input\": \"invalid_string_test_case_<>\" }";
            case "XML External Entity Vulnerabilities in HTTP Body":
                return "{ \"test_case\": \"" + testCaseName + "\", \"xml\": \"<?xml version=\\\"1.0\\\" encoding=\\\"ISO-8859-1\\\"?><!DOCTYPE foo [<!ELEMENT foo ANY ><!ENTITY xxe SYSTEM \\\"file:///etc/passwd\\\" >]><foo>&xxe;</foo>\" }";
            case "Cross Site Tracing Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"trace\": \"TRACE / HTTP/1.1\\r\\nHost: example.com\" }";
            case "Response Body containing Non-HTTPS links":
                return "{ \"test_case\": \"" + testCaseName + "\", \"links\": \"http://example.com\" }";
            case "Directory Traversal Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"path\": \"../../etc/passwd\" }";
            case "Insecure Deserialization Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"data\": \"O:7:\\\"ExampleClass\\\":1:{s:4:\\\"test\\\";s:4:\\\"test\\\";}\" }";
            case "Unvalidated Redirects Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"redirect\": \"http://malicious.com\" }";
            case "Broken Authentication Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"auth\": \"Bearer invalid_token\" }";
            case "Sensitive Data Exposure Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"data\": \"{\\\"password\\\":\\\"123456\\\"}\" }";
            case "Broken Access Control Vulnerabilities":
                return "{ \"test_case\": \"" + testCaseName + "\", \"access\": \"admin_page\" }";
            default:
                return "{ \"test_case\": \"" + testCaseName + "\" }";
        }
    }

    private static void performHttpRequest(String apiUrl, String requestData) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write request data
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestData.getBytes("UTF-8"));
                os.flush();
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "HTTP Response Code: " + responseCode);

            // Read and log the response
            try (InputStream is = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                Log.d(TAG, "Response: " + responseBuilder.toString());
            }

        } catch (Exception e) {
            Log.e(TAG, "Error during HTTP request: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String generateLargeString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("A");
        }
        return sb.toString();
    }

    private static String generateDeepJson(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < depth; i++) {
            sb.append("\"nested\":{");
        }
        sb.append("\"end\":\"true\"");
        for (int i = 0; i < depth; i++) {
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }
}
