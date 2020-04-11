package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController {
    private  CardView view;

    CardController(CardView view) {
        this.view = view;
        view.addLoginListener(new LoginController.LoginButtonListener());
        view.addRegisterListener(new LoginController.RegisterButtonListener());
    }

    static class LoginButtonListener implements ActionListener {
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
}
