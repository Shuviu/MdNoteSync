package org.example;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Logger {
    public static String pathToLogFile = "log.txt";
    static void logException(Exception exception, String location) {
        try(FileWriter fileWriter = new FileWriter(pathToLogFile, true)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis()));
            bufferedWriter.newLine();
            bufferedWriter.append(location);
            bufferedWriter.newLine();
            bufferedWriter.append(exception.getMessage());
            bufferedWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
