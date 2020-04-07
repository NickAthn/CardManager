package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterController {
    private RegisterView view;

    RegisterController(RegisterView view) {
        this.view = view;

    }

    static class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }
    class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }
}
