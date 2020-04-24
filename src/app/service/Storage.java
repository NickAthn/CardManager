package app.service;
import app.AppState;
import app.model.Card;
import app.model.User;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;
import app.util.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ArrayList;
import java.util.UUID;

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
    final static public String appPrivateKeyPath = keysDir + "app_rsa_private.der";
    final static public String appPublicKeyPath = keysDir + "app_rsa_public.der";

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
        userDir = dataDir + "UsersData/" + user.username + "/";
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
            if (user.username.equals(username)) {
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

    public User updateUser(User user) {
        return null;
    }
    public void deleteUser(String username) {

    }

    // CARDS CRUD
    public void createCard(Card card){

    }
    public Card readCard(){
        return null;
    }
    public void updateCard(String cardNumber){

    }
    public void deleteCard(String cardNumber){

    }

    public ArrayList<Card> readAllCards() throws Exception {
        ArrayList<Card> allCards = new ArrayList<>();
        Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                .filter(Files::isRegularFile)
                .forEach( path -> {
                    try {
                        AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                        Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                        allCards.add(card);
                        System.out.println(card.getCardholder() + " // " + card.getType());
                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                } );
        return allCards;
    }

    // User Encrypted Keys
    public void saveKeyBytes(String username, byte[] keyBytes) throws IOException {
        FileUtils.saveData(keyBytes, Storage.keysDir + username + "_encrypted_secret.key");
    }
    public byte[] readUserKeyBytes(String username) throws IOException {
        return FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
    }

    public void saveKey(Key key, String title) throws IOException {
        FileUtils.saveData(key.getEncoded(),keysDir + title);
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
    public static String getCardPath(String username) throws IOException {
        return getCardsDir(username) + UUID.randomUUID().toString();
    }

    @SuppressWarnings("unchecked") // Suppress warning for unchecked cast
    public ArrayList<Card> readCards()  {
        try {
            return (ArrayList<Card>) FileUtils.readObject(cardsPath);
        } catch (ClassNotFoundException | IOException e) {
            return new ArrayList<>();
        }
    }


}
