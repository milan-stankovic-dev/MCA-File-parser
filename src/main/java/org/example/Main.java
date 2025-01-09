package org.example;

import lombok.val;
import net.querz.mca.Chunk;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import java.io.File;
import java.lang.reflect.Field;

import static org.example.constants.FilePaths.FILE_LOCATION;

public class Main {
    
    private static String intArrToString(int[] array) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[ " );
        int i = 0;
        for(;i<array.length-1;i++) {
            builder.append(array[i])
                    .append(", ");
        }
        builder.append(array[i]).append(" ]");
        
        return builder.toString();
    }
    public static void main(String[] args) {
        processSingleMCAFile(new File(FILE_LOCATION));
    }
    
    private static void processSingleMCAFile(File mcaFileAsGeneric) {
        try {
            val mcaFile = MCAUtil.read(mcaFileAsGeneric);
            for (int chunkX = 0; chunkX < 32; chunkX++) {
                for (int chunkZ = 0; chunkZ < 32; chunkZ++) {
                    val chunk = mcaFile.getChunk(chunkX, chunkZ);

                    if (chunk == null) {
                        System.out.println("Chunk at (" + chunkX + ", " + chunkZ + ") is empty.");
                        continue;
                    }

                    final Field chunkDataField = Chunk.class.getDeclaredField("data");
                    chunkDataField.setAccessible(true);
                    final CompoundTag data = (CompoundTag) chunkDataField.get(chunk);

                    final CompoundTag structures = data.getCompoundTag("structures");
                    if (structures != null) {
                        final CompoundTag starts = structures.getCompoundTag("starts");
                        if (starts != null) {
                            for (val key : starts.keySet()) {
                                System.out.println("=== CHUNK (X, Z) = (" + chunkX + ", " + chunkZ + ") ===");
                                System.out.println("FOUND STRUCTURE TYPE: " + key);
                                final CompoundTag structureData = (CompoundTag) starts.get(key);

                                val children = structureData.getListTag("Children");

                                for(val child : children) {
                                    val bbData = ((CompoundTag) child).getIntArray("BB");

                                    final String bbString = intArrToString(bbData);
                                    System.out.println("BOUNDING BOX COORDS: " + bbString);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading MCA file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}