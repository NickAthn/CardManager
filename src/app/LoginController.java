package app;

import app.service.Authenticator;

import java.awt.event.*;

public class LoginController {
    private LoginView view;

    LoginController(LoginView view) {
        this.view = view;
        view.addLoginListener(new LoginButtonListener());
        view.addRegisterListener(new RegisterButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class LoginButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Authenticator.getInstance().authenticate(view.getUsernameInput(),view.getPasswordInput());
                HomeView view = new HomeView();
                HomeController controller = new HomeController(view);
                view.show();
            } catch (Exception ex) {
                showMessage(ex.getMessage(),"Login Failed");
            }
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
