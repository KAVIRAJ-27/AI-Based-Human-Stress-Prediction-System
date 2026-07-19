package com.stress.controller;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class AuthControllerJavaClientTest {

    @Test
    void sendsLoginRequestViaHttpURLConnection() throws Exception {
        URL url = new URL("http://localhost:8080/api/auth/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String body = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        System.out.println("STATUS=" + status);
        if (connection.getErrorStream() != null) {
            System.out.println(new String(connection.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
        }
    }
}
