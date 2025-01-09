package reader;

import lombok.RequiredArgsConstructor;
import net.querz.io.Deserializer;
import net.querz.nbt.io.NamedTag;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class MCAReader {
    private static final String FILE_LOCATION = "/home/milan/Desktop/ATLauncher/instances/Minecraft120/saves/adding extra april fools/region/r.0.2.mca";
    private final Deserializer deserializer;
    
    public void readData() {
        try {
            final Object data = deserializer.fromFile(new File(FILE_LOCATION));
            System.out.println(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
