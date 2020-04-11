package app.service;

import app.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Authenticator {
    private Storage storage;
    private InformationShow viewController;
    private Cryptographer crypto;

    public Authenticator(InformationShow viewController){
        this.storage = new Storage("db.txt");
        this.viewController = viewController;
        this.crypto = new Cryptographer();
    }

    // Login
    public boolean loginWith(String username, char[] password) {
        AtomicReference<Boolean> shouldLogin = new AtomicReference<>(false);
        ArrayList<User> userList = getUsers();
        userList.forEach( userS -> {
            if (userS.username.equals(username)) {
                if (Cryptographer.validatePassword(password, userS.password)) {
                    shouldLogin.set(true);
                }
            }
        });
        if (!shouldLogin.get()) {
            showMessage("Wrong username or password", "Failed Login");
        }
        return  shouldLogin.get();
    }
    // Registers a new user
    public void registerUser(String name, String email, String username, char[] password) {
        ArrayList<User> userList = getUsers();
        if (!isUsernameAvailable(username)) {
            showMessage("Username already exists!", "Username Error");
        } else {
            String hashedPassword = crypto.hash(password);
            User newUser = new User(username, hashedPassword, name, email);
            userList.add(newUser);
            try {
                storage.saveUsers(userList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Reads all users from
    public ArrayList<User> getUsers() {
        try {
            return storage.readUsers();
        } catch (IOException e) {
            // If file is not found return an empty array
            return new ArrayList<>();
        }
    }

    // Checks if username is unique
    public boolean isUsernameAvailable(String username) {
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);
        ArrayList<User> userList = getUsers();

        userList.forEach(user -> {
            if(user.username.equals(username)) {
                isFound.set(true);
            }
        });
        return !isFound.get();
    }

    void showMessage(String message, String title) {
        viewController.showMessage(message,title);
    }
}
