package app.viewController;

import app.service.Authenticator;
import app.view.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterController{
    private RegisterView view;

    RegisterController(RegisterView view) {
        this.view = view;
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
            try {
                Authenticator.getInstance().register(view.getNameInput(),view.getEmailInput(),view.getUsernameInput(),view.getPasswordInput());
            } catch (Exception e) {
                view.showMessage(e.getMessage(),"Failed to register");
            }
        }
    }
    class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }


}
