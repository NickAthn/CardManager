package app;

import app.service.Authenticator;
import app.service.InformationShow;

import java.awt.event.*;

public class LoginController implements InformationShow {
    private LoginView view;
    private Authenticator authenticator;

    LoginController(LoginView view) {
        this.view = view;
        this.authenticator = new Authenticator(this);
        view.addLoginListener(new LoginButtonListener());
        view.addRegisterListener(new RegisterButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class LoginButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            // TODO: Change password implementation
            authenticator.loginWith(view.getUsernameInput(),new String(view.getPasswordInput()));
        }
    }

    static  class RegisterButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) { // Upon register button press show registerView
            RegisterView view = new RegisterView();
            RegisterController controller = new RegisterController(view);
            view.show();
        }
    }
}
