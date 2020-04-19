package app;

import app.service.Authenticator;
import app.service.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveController implements View {
    private RemoveView view;
    private Authenticator authenticator;

    RemoveController(RemoveView view) {
        this.view = view;
        this.authenticator = new Authenticator(this);

        setupListeners();
    }

    void setupListeners() {
        view.addRemoveListener(new RemoveButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Change Password implementation

        }
    }
    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }


}
