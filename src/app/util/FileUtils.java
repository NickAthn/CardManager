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
     * @param filePath the path where the object is saved
     */
    public static void saveObject(Object obj, String filePath ) throws IOException {
        FileOutputStream fop = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fop);
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Reads an object from given path
     *
     * @param filePath the path to read from
     * @return Object that can be cast to anything
     */
    public static Object readObject(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static void saveData(byte[] data, String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        out.write(data);
        out.close();
    }

    public static byte[] readData(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}
