package app;

import java.awt.event.*;

public class LoginController {
    private  LoginView view;

    LoginController(LoginView view) {
        this.view = view;
        view.addLoginListener(new LoginButtonListener());
        view.addRegisterListener(new RegisterButtonListener());
    }

    static class LoginButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login Button Pressed");
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
