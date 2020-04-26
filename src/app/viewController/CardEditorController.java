package app.viewController;


import app.AppState;
import app.model.Card;
import app.service.security.AESCryptographer;
import app.view.CardEditorView;
import app.service.Storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CardEditorController {
    private CardEditorView view;
    private Card card;

    public CardEditorController(CardEditorView view)  {
        this.view = view;
        card = new Card();
        setupListeners();
    }
    public CardEditorController(Card card, CardEditorView view) {
        this.card = card;
        this.view = view;
        this.view.setCVCField(card.getCvc());
        this.view.setExpirationDateField(card.getExpirationDate().toString());
        this.view.setNumberField(card.getNumber());
        this.view.setTypeField(card.getType());
        this.view.setCardholderField(card.getCardholder());
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            card.setCardholder(view.getCardUserInput());
            card.setCvc(view.getCardCvcInput());
            card.setExpirationDate(view.getCardDateInput());
            card.setNumber(view.getCardNumInput());
            card.setType(view.getCardTypeInput());

            try {
                Files.deleteIfExists(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername()) + card.getId()));

                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                aes.encryptAndSerialize(card,new FileOutputStream(Storage.getCardsDir(AppState.getInstance().getSession().getUsername()) + card.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class BackButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}

