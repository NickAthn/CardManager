package app.service;
import app.model.Card;
import app.model.User;
import app.util.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
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
    public Storage() {
        // Creating the directories
        FileUtils.createDirectory(dataDir);
        FileUtils.createDirectory(keysDir);
    }

    // Logged user init
    public Storage(User user)  {
        super();
        userDir = dataDir + "UsersData/" + user.username + "/";
        cardsPath = userDir + "userCards.txt";
//        FileUtils.createDirectory(userDir);
        try {
            Files.createDirectories(Paths.get(userDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Logic handled by Authenticator
    public void saveUsers(ArrayList<User> usersList) throws IOException {
        FileUtils.saveObject(usersList, dbPath);
    }
    @SuppressWarnings("unchecked") // Suppress warning for unchecked cast
    public ArrayList<User> readUsers() throws IOException, ClassNotFoundException {
        return (ArrayList<User>) FileUtils.readObject(dbPath);
    }


    public void saveKey(Key key, String title) throws IOException {
        FileUtils.saveData(key.getEncoded(),keysDir + title);
    }

    public void saveCard(Card card) throws Exception {
        if (userDir != null) {
            ArrayList<Card> cardsList = readCards();
            cardsList.add(card);
            FileUtils.saveObject(cardsList, cardsPath);
        } else {
            throw new Exception("User path not find. User is probably not logged");
        }
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
