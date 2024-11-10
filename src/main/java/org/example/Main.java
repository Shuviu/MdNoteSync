package org.example;

import org.example.Dataclasses.File;
public class Main {
    public static void main(String[] args) {
        String pathToNotesDirectory = "pathToDirectory";
        String pathToLastSyncTimestamp = "timestamp.txt";

        boolean connectionEstablished = DBManager.establishConnection(0);
        if (!connectionEstablished) {
            throw new RuntimeException("Connection could not be established.");
        }
        System.out.println("Database connection successfully established");

        org.example.Dataclasses.File[] notes = FileManager.readFiles(
                pathToNotesDirectory,
                pathToLastSyncTimestamp);
        for (File note : notes) {
            boolean entry_removed = DBManager.removeEntry(note);
            System.out.println("Entry removed: " + entry_removed);

            boolean entry_inserted = DBManager.insertEntry(note);
            System.out.println("Entry inserted: " + entry_inserted);
            System.out.println(note.location);
        }
        FileManager.updateLastSyncTimestamp(pathToLastSyncTimestamp);
        DBManager.closeConnection();
    }
}