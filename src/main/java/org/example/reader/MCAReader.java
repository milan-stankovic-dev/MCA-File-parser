package org.example.reader;

import lombok.Getter;
import lombok.val;
import net.querz.mca.Chunk;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.EOFException;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

public class MCAReader {
    @Getter
    private static final MCAReader instance = new MCAReader();
    
    private MCAReader() { }
    
    public void findStructuresInFolder(String folderPath) {
        val folder = new File(folderPath);

        final File[] mcaFiles = folder.listFiles((dir, name) -> name.endsWith(".mca"));

        if(mcaFiles == null || mcaFiles.length == 0) {
            throw new IllegalStateException("Could not find any mca data. Try again.");
        }

        Arrays.stream(mcaFiles).forEach(this::findStructuresInOneMCA);
    }
    
    public void findStructuresInOneMCA(File genericFileType) {
        try {
            val mcaFile = MCAUtil.read(genericFileType);
            for (int chunkX = 0; chunkX < 32; chunkX++) {
                for (int chunkZ = 0; chunkZ < 32; chunkZ++) {
                    val chunk = mcaFile.getChunk(chunkX, chunkZ);

                    if (chunk == null) {
//                        System.out.println("Chunk at (" + chunkX + ", " + chunkZ + ") is empty.");
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
                                if (children == null) {
                                    System.out.println("COULD NOT FIND CHILDREN! DATA: " + structureData);
                                    continue;
                                }
                                for (val child : children) {

                                    if (child == null) {
                                        System.out.println("CHILD TAG FOUND TO BE NULL!");
                                        continue;
                                    }

                                    val bbData = ((CompoundTag) child).getIntArray("BB");

                                    final String bbString = intArrToString(bbData);
                                    System.out.println("BOUNDING BOX COORDS: " + bbString);
                                }
                            }
                        }
                    }
                }
            }
        }catch (EOFException ex) {
            System.out.println("EOF exception while reading mca file. Corrupted data?");
        } catch (Exception e) {
            System.err.println("Error reading MCA file: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
}
