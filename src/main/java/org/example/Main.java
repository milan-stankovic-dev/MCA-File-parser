package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import org.example.constants.Structures;
import org.example.reader.MCAReader;

import java.util.List;

import static org.example.constants.Structures.STRONGHOLD;

public class Main {
    public static void main(String[] args) {
        final MCAReader reader = MCAReader.getInstance();
        val dotenv = Dotenv.load();
        
        final String folderPath = dotenv.get("FOLDER_PATH");
        reader.findStructuresInFolder(folderPath, List.of(Structures.));
    }
}