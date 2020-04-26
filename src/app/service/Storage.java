package app.service;
import app.model.User;
import app.util.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 *  Storage is responsible for all the applications storage needs.
 *
 */
public class Storage {
    // Userless paths
    final static public String dataDir = "Data/";
    final static public String keysDir = dataDir + "Keys/";
    final static public String dbPath =  dataDir + "userDatabase.txt";

    // Logged user required paths
    public String userDir = null;
    public String cardsPath = null;

    // Userless init
    public Storage() throws IOException {
        // Creating the directories
        Files.createDirectories(Paths.get(dataDir));
        Files.createDirectories(Paths.get(keysDir));
    }

    // Logged user init
    public Storage(User user) throws IOException {
        super();
        userDir = dataDir + "UsersData/" + user.getUsername() + "/";
        cardsPath = userDir + "userCards.txt";
    }

    // USER CRUD OPERATIONS
    public void createUser(User user) throws IOException {
        ArrayList<User> allUsers = readAllUsers();
        allUsers.add(user);
        FileUtils.saveObject(allUsers,dbPath);
    }
    public User readUser(String username) {
        ArrayList<User> allUsers = readAllUsers();
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked") // Suppress warning for unchecked cast
    private ArrayList<User> readAllUsers() {
        try {
            return (ArrayList<User>) FileUtils.readObject(dbPath);
        } catch (IOException | ClassNotFoundException e) {
            // If file is not found return an empty array
            return new ArrayList<>();
        }
    }


    // User Encrypted Keys
    public void saveKeyBytes(String username, byte[] keyBytes) throws IOException {
        FileUtils.saveBytes(keyBytes, Storage.keysDir + username + "_encrypted_secret.key");
    }
    public byte[] readUserKeyBytes(String username) throws IOException {
        return FileUtils.readBytes(Storage.keysDir + username + "_encrypted_secret.key");
    }

    public static String getUserDir(String username) throws IOException {
        String dir = dataDir + "UsersData/" + username + "/";
        Files.createDirectories(Paths.get(dir));
        return dataDir + "UsersData/" + username + "/";
    }
    public static String getCardsDir(String username) throws IOException {
        String dir = getUserDir(username) + "Cards/";
        Files.createDirectories(Paths.get(dir));
        return dir;
    }


}
