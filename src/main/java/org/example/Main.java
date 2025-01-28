package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import org.example.reader.MCAReader;

import static org.example.constants.Structures.*;

public class Main {
    public static void main(String[] args) {
        final MCAReader reader = MCAReader.getInstance();
        val dotenv = Dotenv.load();
        
        final String folderPath = dotenv.get("OVERWORLD_PATH");
        System.out.println(folderPath);
        reader.findStructuresInFolder(folderPath, SWAMP_HUT);
    }
}