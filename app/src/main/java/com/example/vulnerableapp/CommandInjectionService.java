package com.example.vulnerableapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandInjectionService {
    public static void executeCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = reader.readLine();
        System.out.println(result);
    }
}
