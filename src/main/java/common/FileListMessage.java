package common;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileListMessage extends AbstractMessage{
    private List<String> fileList;

    public FileListMessage(List<String> fileList) {
        this.fileList = fileList;
    }

    public FileListMessage() {

    }
//    public List<String> getFileList() {
//        return fileList;
//    }

    //List<String> fileNames = new ArrayList<>();
    public static List<String>getFileList(String directory) {
        List<String> fileNames = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory));
            for (Path path : directoryStream) {
                fileNames.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public List<String>getFiles(){
        return fileList;
    }
}
