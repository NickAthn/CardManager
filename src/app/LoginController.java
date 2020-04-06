package app;

import java.awt.event.*;

public class LoginController {
    private  LoginView view;

    LoginController(LoginView view) {
        this.view = view;
        view.addLoginListener(new LoginButtonListener());
    }

    static class LoginButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login Button Pressed");
        }
    }
}
