package app.service;
import app.model.User;

import java.io.*;
import java.util.ArrayList;

public class Storage {
    String dbPath;

    public Storage(String dbPath){
        // Check if DB file exists
        this.dbPath = dbPath;

    }

    void saveUsers(ArrayList<User> usersList) throws IOException {
        FileOutputStream fop = new FileOutputStream(dbPath);
        ObjectOutputStream oos = new ObjectOutputStream(fop);
        oos.writeObject(usersList);
    }

    ArrayList<User> readUsers() throws IOException {
        FileInputStream fis = new FileInputStream(dbPath);
        ObjectInputStream ois = new ObjectInputStream(fis);

        ArrayList<User> usersList = new ArrayList<>();
        try {
            usersList = (ArrayList<User>) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  usersList;
    }

}
