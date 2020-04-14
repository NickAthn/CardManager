package app.service;
import app.model.User;
import app.util.FileUtils;

import java.io.*;
import java.security.Key;
import java.util.ArrayList;

/**
 *
 *  Storage is responsible for all the applications storage needs.
 *
 */
public class Storage {
    final static public String dataDir = "Data/";
    final static public String keysDir = dataDir + "Keys/";
    final static public String dbPath =  dataDir + "userDatabase.txt";

    public Storage() {
        // Creating the directories
        FileUtils.createDirectory(dataDir);
        FileUtils.createDirectory(keysDir);
    }

    public static void saveUsers(ArrayList<User> usersList) throws IOException {
        FileUtils.saveObject(usersList, dbPath);
    }

    @SuppressWarnings("unchecked") // Suppress warning for unchecked cast
    public ArrayList<User> readUsers() throws IOException, ClassNotFoundException {
        return (ArrayList<User>) FileUtils.readObject(dbPath);
    }

    public void saveKey(Key key, String title) throws IOException {
        FileUtils.saveData(key.getEncoded(),keysDir + title);
    }

    public void saveFile(byte[] data, String filePath) {

    }
}
