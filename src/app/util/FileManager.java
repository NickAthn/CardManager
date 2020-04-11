package app.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    /**
     * Create a dir with specific name
     *
     * @param dirName
     */
    public static void CreateDir(String dirName) {

        // Get working directory
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String localFullPath = s + "/" + dirName;

        File theDir = new File(localFullPath);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("[+] Creating directory: " + theDir.getName());
            boolean result = false;
            try
            {
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result)
            {
                System.out.println("DIR created");
            }
        }
    }

}
