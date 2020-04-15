package app;


import app.service.CardManager;
import app.service.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardController implements View{
    private  CardView view;
    private  CardManager cardmanager;

    CardController(CardView view) {
        this.view = view;
        this.cardmanager = new CardManager(this);
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    @Override
    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            cardmanager.addCard(view.getCardNumInput(),view.getCardUserInput(),view.getCardTypeInput(),view.getCardCvcInput(),view.getCardDateInput());
        }
    }

    class BackButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}

