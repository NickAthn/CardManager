package app;

import app.model.User;
import app.service.Authenticator;
import app.service.InformationShow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterController implements InformationShow {
    private RegisterView view;
    private Authenticator authenticator;

    RegisterController(RegisterView view) {
        this.view = view;
        this.authenticator = new Authenticator(this);

        setupListeners();
    }

    void setupListeners() {
        view.addRegisterListener(new RegisterButtonListener());
        view.addCancelListener(new CancelButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Change Password implementation
            authenticator.registerUser(new User(view.getUsernameInput(),new String(view.getPasswordInput()),view.getNameInput(),view.getEmailInput()));
        }
    }
    class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }


}
