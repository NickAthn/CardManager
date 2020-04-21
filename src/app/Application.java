package app;

import app.service.Storage;
import app.service.security.RSACryptographer;
import app.view.LoginView;
import app.viewController.LoginController;

import java.io.IOException;

public class Application {
    static final String privateKeyPath = "Data/Keys/app_rsa_private.der";
    static final String publicKeyPath = "Data/Keys/app_rsa_public.der";

    public static void main(String[] args) throws IOException {
        // Getting the cryptographer ready
        AppState.getInstance().setAppCryptographer(new RSACryptographer(privateKeyPath,publicKeyPath));
        AppState.getInstance().setStorage(new Storage());

        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.show();
    }
}
