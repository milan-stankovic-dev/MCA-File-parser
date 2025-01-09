package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import org.example.reader.MCAReader;

public class Main {
    public static void main(String[] args) {
        final MCAReader reader = MCAReader.getInstance();
        val dotenv = Dotenv.load();
        
        final String folderPath = dotenv.get("FOLDER_PATH");
        reader.findStructuresInFolder(folderPath);
    }
}