package app.viewController;

import app.AppState;
import app.model.User;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;
import app.util.PasswordUtils;
import app.view.RegisterView;

import javax.crypto.SecretKey;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RegisterController{
    private RegisterView view;

    RegisterController(RegisterView view) {
        this.view = view;
        setupListeners();
    }

    void setupListeners() {
        view.addRegisterListener(new RegisterButtonListener());
        view.addCancelListener(new CancelButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                Storage storage = AppState.getInstance().getStorage();
                RSACryptographer rsa = AppState.getInstance().getAppCryptographer();

                if (storage.readUser(view.getUsernameInput()) != null) throw new Exception("Username not available");
                String encryptedHashedPassword = hashAndEncrypt(view.getPasswordInput());
                User newUser = new User(view.getUsernameInput(), encryptedHashedPassword, view.getNameInput(), view.getEmailInput());
                storage.createUser(newUser);
                // After successful creation also create user AES Key
                SecretKey userKey = AESCryptographer.generateKey();
                assert userKey != null;
                byte[] encryptedUserKey = rsa.encrypt(userKey.getEncoded(),RSACryptographer.USE_PUBLIC_KEY);
                storage.saveKeyBytes(newUser.getUsername(),encryptedUserKey);
                showMessage("User registered!", "Success");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage(),"Failed to register user.");
            }
        }
    }
    class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }

    String hashAndEncrypt(char[] password) throws Exception {
        RSACryptographer rsa = AppState.getInstance().getAppCryptographer();
        String hashedPassword = PasswordUtils.hash(password);
        assert hashedPassword != null;
        return Base64.getEncoder().encodeToString(rsa.encrypt(hashedPassword.getBytes(StandardCharsets.UTF_8), RSACryptographer.USE_PUBLIC_KEY));
    }
}
