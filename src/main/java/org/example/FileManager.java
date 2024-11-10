package org.example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FileManager {

    // read in new / edited notes
    public static org.example.Dataclasses.File[] readFiles(String pathToNoteDirectory,String pathToTimestamp){
        File[] notes;
        try {
            // fetch last timestamp of the last sync
            Date lastTimeChecked = getLastCheckedTimestamp(pathToTimestamp);

            // read in and sort all notes of the directory
            File noteDir = new File(pathToNoteDirectory);
            notes = noteDir.listFiles(note -> note.isFile()
                && note.lastModified() > lastTimeChecked.getTime()
            );
        }
        catch (Exception e) {
            notes = new File[0];
            Logger.logException(e, "Read in notes stored in the directory: ");
        }
        // return notes
        return convertToFileClass(notes);
    }

    // Read in the "last time checked" timestamp stored in the .txt file
    private static Date getLastCheckedTimestamp(String pathToTimestamp) {

        try(FileReader fileReader = new FileReader(pathToTimestamp)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // read in the content and parse to Date
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String lastTimeChecked = bufferedReader.readLine();
            return dateFormat.parse(lastTimeChecked);

        } catch (Exception e) {
            Logger.logException(e, "Read in LastSyncTimestamp: ");
            return null;
        }
    }

    public static org.example.Dataclasses.File[] convertToFileClass(File[] files) {
        // Declare new notes array
        org.example.Dataclasses.File[] notes = new org.example.Dataclasses.File[files.length];

        for (int i = 0; i < files.length; i++) {
            // read in all notes
            try(FileReader filedReader = new FileReader(files[i].getAbsolutePath())){
                BufferedReader bufferedReader = new BufferedReader(filedReader);

                // declare content string
                String currentLine;
                StringBuilder fileContent = new StringBuilder();

                // query whole note content and append to string
                while ((currentLine = bufferedReader.readLine()) != null) {
                    fileContent.append(currentLine);
                }

                // convert to file Class
                notes[i] = new org.example.Dataclasses.File(files[i].getName(), files[i].getAbsolutePath(), fileContent.toString());
            }
            catch (Exception e) {
                Logger.logException(e, "Converting read in notes to File datatype: ");;
            }
        }
        return notes;
    }

    public static void updateLastSyncTimestamp(String pathToTimestamp){
        try(FileWriter fileWriter = new FileWriter(pathToTimestamp)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis()));
            bufferedWriter.close();
        }
        catch (Exception e) {
            Logger.logException(e, "Updating LastSyncTimestamp: ");
        }
    }
}
