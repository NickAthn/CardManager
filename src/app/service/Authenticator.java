package app.service;

import app.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Authenticator {
    private Storage storage;
    private InformationShow viewController;

    public Authenticator(InformationShow viewController){
        this.storage = new Storage("db.txt");
        this.viewController = viewController;
    }

    // Login
    public boolean loginWith(String username, String password) {
        AtomicReference<Boolean> shouldLogin = new AtomicReference<>(false);
        ArrayList<User> userList = getUsers();
        userList.forEach( userS -> {
            if (userS.username.equals(username)) {
                if (userS.password.equals(password)) {
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
    public void registerUser(User user) {
        ArrayList<User> userList = getUsers();
        if (!isUsernameAvailable(user.username)) {
            showMessage("Username already exists!", "Username Error");
        } else {
            userList.add(user);
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
