package app.service;

import app.model.User;
import app.util.PasswordUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class Authenticator {
    private Storage storage;
    private View activeView;
    private Cryptographer crypto;

    public Authenticator(View activeView){
        this.storage = new Storage();
        this.activeView = activeView;
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
        return shouldLogin.get();
    }
    // Registers a new user
    public void registerUser(String name, String email, String username, char[] password) {
        ArrayList<User> userList = getUsers();
        if (!isUsernameAvailable(username)) {
            showMessage("Username already exists!", "Username Error");
        } else {
            try {

                String hashedPassword = PasswordUtils.hash(password);
                assert hashedPassword != null;
                String encryptedPassword = Base64.getEncoder().encodeToString(crypto.encrypt(hashedPassword.getBytes(StandardCharsets.UTF_8)));
                User newUser = new User(username, encryptedPassword, name, email);
                userList.add(newUser);
                storage.saveUsers(userList);
                crypto.createKeyForUser(username);
            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
    }

    // Reads all users from
    public ArrayList<User> getUsers() {
        try {
            return storage.readUsers();
        } catch (IOException | ClassNotFoundException e) {
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
        activeView.showMessage(message,title);
    }
}
