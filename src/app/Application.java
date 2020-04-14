package app;

import app.service.Cryptographer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Cryptographer crypto = new Cryptographer();

        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.show();
    }
}
