package common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {
    public static void insertToLogs(String filePath, String lineToInsert) {
        File file = new File(filePath);

        try {
            // Check if the file exists, if not, create it
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File did not exist, so it was created.");
            }

            // Create a FileWriter in append mode to add a line without overwriting the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            // Insert the new line
            writer.newLine();
            writer.write(lineToInsert);
            writer.close();

            System.out.println("Line inserted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
