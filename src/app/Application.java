package app;

import app.model.Card;
import app.model.User;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;
import app.service.security.SecurityUtils;
import app.util.FileUtils;
import app.view.LoginView;
import app.viewController.LoginController;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;

public class Application {
    static final String privateKeyPath = "Data/Keys/app_rsa_private.der";
    static final String publicKeyPath = "Data/Keys/app_rsa_public.der";

    public static void main(String[] args) throws IOException {
        // Getting the cryptographer and storage ready
        AppState.getInstance().setStorage(new Storage());
        AppState.getInstance().setAppCryptographer(new RSACryptographer(privateKeyPath,publicKeyPath));

        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.show();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    if (AppState.getInstance().getStorage() != null) {
                        integritySign();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Shutdown-thread"));
    }

    /**
     * Sings user directory for unauthorised tempering
     */
    static void integritySign() throws Exception {
        RSACryptographer rsa = AppState.getInstance().getAppCryptographer();
        AESCryptographer aes = AppState.getInstance().getUserCryptographer();
        User user = AppState.getInstance().getSession();
        String cardsDigest = SecurityUtils.getDirectoryChecksum(Storage.getCardsDir(user.getUsername()));
        String digest = SecurityUtils.hash(new Pair<>(user.getUsername(),cardsDigest).toString().getBytes());
        String signature = rsa.sign(digest.getBytes());
        aes.encryptAndSerialize(signature,new FileOutputStream( Storage.getUserDir(user.getUsername()) + "data_signature"));
    }
    /**
     * Checks user directory for unauthorised tempering
     */
    public static boolean integrityCheck() throws Exception {
        RSACryptographer rsa = AppState.getInstance().getAppCryptographer();
        AESCryptographer aes = AppState.getInstance().getUserCryptographer();
        User user = AppState.getInstance().getSession();
        String signature = (String) aes.decryptAndDeserialize(new FileInputStream(Storage.getUserDir(user.getUsername()) + "data_signature"));
        String cardsDigest = SecurityUtils.getDirectoryChecksum(Storage.getCardsDir(user.getUsername()));
        String digest = SecurityUtils.hash(new Pair<>(user.getUsername(),cardsDigest).toString().getBytes());
        return rsa.verify(digest.getBytes(), signature);
    }
}
