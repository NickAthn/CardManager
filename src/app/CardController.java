package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController {
    private  CardView view;

    CardController(CardView view) {
        this.view = view;
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    static class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login Button Pressed");
        }
    }

    class BackButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}

