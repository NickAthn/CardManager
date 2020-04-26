package app.viewController;

import app.AppState;
import app.Application;
import app.model.User;
import app.service.Storage;
import app.service.security.RSACryptographer;
import app.service.security.PasswordUtils;
import app.view.RegisterView;
import app.view.HomeView;
import app.view.LoginView;

import java.awt.event.*;
import java.util.Base64;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        view.addLoginListener(new LoginButtonListener());
        view.addRegisterListener(new RegisterButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Storage storage = AppState.getInstance().getStorage();
                RSACryptographer rsa = AppState.getInstance().getAppCryptographer();

                User user = storage.readUser(view.getUsernameInput());
                if (user == null) throw new Exception("Username doesn't exist"); // Auth failed
                String hashedPassword = new String(rsa.decrypt(Base64.getDecoder().decode(user.getPassword()), RSACryptographer.USE_PRIVATE_KEY));
                if (PasswordUtils.validatePassword(view.getPasswordInput(), hashedPassword)) {
                    // Authentication Successful
                    AppState.getInstance().setSession(user);
                    HomeView view = new HomeView();
                    HomeController controller = new HomeController(view);
                    view.show();
                    disposeView();
                } else {
                    // Auth failed
                    throw new Exception("Password is incorrect");
                }
            } catch (Exception ex) {
                // Auth failed
                showMessage(ex.getMessage(), "Login Failed");
            }
        }
    }

    static class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { // Upon register button press show registerView
            RegisterView view = new RegisterView();
            RegisterController controller = new RegisterController(view);
            view.show();
        }
    }

    private void disposeView() {
        view.dispose();
    }
}
