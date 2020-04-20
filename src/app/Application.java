package app;

import app.service.security.RSACryptographer;
import app.view.LoginView;
import app.viewController.LoginController;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class Application {
    static final String privateKeyPath = "Data/Keys/app_rsa_private.der";
    static final String publicKeyPath = "Data/Keys/app_rsa_private.der";

    public static void main(String[] args) throws IOException {
        // Getting the cryptographer ready
        RSACryptographer rsaCrypto;
        try {
            PublicKey publicKey = RSACryptographer.readPublicKey(publicKeyPath);
            PrivateKey privateKey = RSACryptographer.readPrivateKey(privateKeyPath);
            KeyPair pair = new KeyPair(publicKey,privateKey);
            if (RSACryptographer.validateKeyPair(pair)) {
                rsaCrypto = new RSACryptographer(pair);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Create new keypair
            rsaCrypto = new RSACryptographer();
            rsaCrypto.savePrivateKey(privateKeyPath);
            rsaCrypto.savePublicKey(publicKeyPath);
        }

        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.show();
    }
}
