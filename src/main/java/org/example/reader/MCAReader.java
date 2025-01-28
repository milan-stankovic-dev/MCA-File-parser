package org.example.reader;

import lombok.Getter;
import lombok.val;
import net.querz.mca.Chunk;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import org.example.constants.Structures;

import java.io.EOFException;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class MCAReader {
    @Getter
    private static final MCAReader instance = new MCAReader();
    
    private MCAReader() { }
    
    public void findStructuresInFolder(String folderPath, Structures... wantedStructures) {
        val folder = new File(folderPath);

        final File[] mcaFiles = folder.listFiles((dir, name) -> name.endsWith(".mca"));

        if(mcaFiles == null || mcaFiles.length == 0) {
            throw new IllegalStateException("Could not find any mca data. Try again.");
        }

        Arrays.stream(mcaFiles).forEach(mcaFile -> findStructuresInOneMCA(mcaFile, wantedStructures));
    }
    
    public void findStructuresInOneMCA(File genericFileType, Structures... wantedStructures) {
        try {
            val mcaFile = MCAUtil.read(genericFileType);
            for (int chunkX = 0; chunkX < 32; chunkX++) {
                for (int chunkZ = 0; chunkZ < 32; chunkZ++) {
                    val chunk = mcaFile.getChunk(chunkX, chunkZ);

                    if (chunk == null) { continue; }

                    final Field chunkDataField = Chunk.class.getDeclaredField("data");
                    chunkDataField.setAccessible(true);
                    final CompoundTag data = (CompoundTag) chunkDataField.get(chunk);

                    final CompoundTag structures = data.getCompoundTag("structures");
                    if (structures != null) {
                        serviceOneChunkWithStructures(structures, chunkX, chunkZ, wantedStructures);
                    }
                }
            }
        }catch (EOFException ignored) {
//            System.out.println("EOF exception while reading mca file. Corrupted data?");
        } catch (Exception e) {
            System.err.println("Error reading MCA file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void serviceOneChunkWithStructures(CompoundTag structures, int chunkX,
                                               int chunkZ, Structures... wantedStructures) {
        
        final CompoundTag starts = structures.getCompoundTag("starts");
        val wantedStructuresList = Arrays.asList(wantedStructures);
        if (starts != null) {
            for (val key : starts.keySet()) {
                if(wantedStructuresList
                        .stream().map(Structures::getValue)
                        .noneMatch(key::contains)) {
                    return;
                }
                System.out.println("=== CHUNK (X, Z) = (" + chunkX + ", " + chunkZ + ") ===");
                System.out.println("FOUND STRUCTURE TYPE: " + key);
                final CompoundTag structureData = (CompoundTag) starts.get(key);

                val children = structureData.getListTag("Children");
                if (children == null) {
                    System.out.println("COULD NOT FIND CHILDREN! DATA: " + structureData);
                    continue;
                }
                for (val child : children) {
                    serviceOneChildTag(child);
                }
                System.out.println("============ END OF STRUCTURE ============");
            }
        }
    }
    
    private void serviceOneChildTag(Object child) {
        if (child == null) {
            System.out.println("CHILD TAG FOUND TO BE NULL!");
            return;
        }

        val bbData = ((CompoundTag) child).getIntArray("BB");

        final String bbString = coordinatesToString(bbData);
        System.out.println("BOUNDING BOX COORDS: " + bbString);
    } 
    
    private String oneSetOfCoordsToString(boolean starting, int... array) {
        final String displayText = starting ? "Starting coords: [ X = " :
                "End coords: [ X = ";

        return new StringBuilder()
                .append(displayText)
                .append(array[0])
                .append(", Y = ")
                .append(array[1])
                .append(", Z = ")
                .append(array[2])
                .append(" ] ").toString();
    }

    private String coordinatesToString(int[] array) {
        return oneSetOfCoordsToString(true,
                array[0], array[1], array[2]) + 
                oneSetOfCoordsToString(false,
                        array[3], array[4], array[5]);
    }
}
