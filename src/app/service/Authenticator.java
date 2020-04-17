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
    // Declaring global instance (singleton)
    private static final Authenticator instance = new Authenticator();
    private Authenticator(){
        this.storage = new Storage();
        this.crypto = new Cryptographer();
    }
    public static Authenticator getInstance() {
        return instance;
    }

    private Storage storage;
    private Cryptographer crypto;

    User session; // The active user. if null then no user is logged.

    // Login
    public void authenticate(String username, char[] password) throws Exception {
        AtomicReference<Boolean> shouldLogin = new AtomicReference<>(false);
        ArrayList<User> userList = getUsers();
        for (User user : userList) {
            if (user.username.equals(username)) {
                if (Cryptographer.validatePassword(password, user.password)) {
                    // Authenticated!
                    shouldLogin.set(true);
                    session = user;
                    break;
                }
            }
        }
        if (!shouldLogin.get()) {
            throw new Exception("Wrong username & password combination");
        }
    }
    // Registers a new user
    public void register(String name, String email, String username, char[] password) throws Exception  {
        ArrayList<User> userList = getUsers();
        if (!isUsernameAvailable(username)) {
           throw new Exception("Username not available.");
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
}
