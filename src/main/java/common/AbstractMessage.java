package common;

import javafx.scene.shape.Path;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractMessage implements Serializable {
    public static void checkDir(String string){
        if (!Files.isDirectory(Paths.get(string))) {
            try {
                Files.createDirectories(Paths.get(string));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
