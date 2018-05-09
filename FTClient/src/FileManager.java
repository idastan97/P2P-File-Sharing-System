


import ftserver.FileInfo;
import ftserver.Peer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManager {
    private static List<FileInfo> allSharedFiles;
    static {
        if(allSharedFiles == null) {
            FileManager.getAllSharedFiles();
        }
    }



    public static List<FileInfo> getAllSharedFiles() {

        // if(allSharedFiles == null)
            allSharedFiles = new ArrayList<>();
        File folder = new File(System.getProperty("user.dir") + "/sharedFiles/");
        File[] allFiles = folder.listFiles();
        for(File file: allFiles)
            if(file.isFile()){
                FileInfo fl = new FileInfo(file.getName().substring(0, file.getName().length() - StringHelper.getType(file.getName()).length() - 1), StringHelper.getType(file.getName()),
                        file.length(), new Date(file.lastModified()), new Peer(null, ClientServer.port));
                allSharedFiles.add(fl);
            }
        return allSharedFiles;
    }


    public static byte[] getFileByFileInfo(FileInfo fi) throws IOException{
        File file = new File(System.getProperty("user.dir") + "/sharedFiles/" + fi.getName() + "." + fi.getType());
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }
}