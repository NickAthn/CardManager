package app.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    /**
     * Checks if the target directory exists and if not creates it.
     *
     * @param targetDirectoryName
     */
    public static void createDirectory(String targetDirectoryName) {
        // Get working directory
        Path workingPath = Paths.get("");
        // Convert working directory format to String
        String workingPathString = workingPath.toAbsolutePath().toString();
        // Construct target directory path
        String fullPath = workingPathString + "/" + targetDirectoryName;
        File targetDirectory = new File(fullPath);

        // Check if directory exists, if not create tt
        if (!targetDirectory.exists()) {
            System.out.println("[+] Creating directory: " + targetDirectory.getName());
            try {
                targetDirectory.mkdir();
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves an object locally to any given path
     *
     * @param obj the object to save
     * @param path the path where the object is saved
     */
    public static void saveObject(Object obj, String path ) throws IOException {
        FileOutputStream fop = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fop);
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Reads an object from given path
     *
     * @param path the path to read from
     * @return Object that can be cast to anything
     */
    public static Object readObject(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static void saveData(byte[] data, String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        out.write(data);
        out.close();
    }

    public static byte[] readFileBytes(String filename) throws IOException {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);
    }
}
