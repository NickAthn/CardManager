package app.viewController;


import app.AppState;
import app.model.Card;
import app.service.security.AESCryptographer;
import app.view.CardEditorView;
import app.service.Storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CardEditorController {
    private CardEditorView view;

    public CardEditorController(CardEditorView view)  {
        this.view = view;
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
            Card newCard = new Card(view.getCardTypeInput(),view.getCardNumInput(),view.getCardUserInput(),view.getCardCvcInput(),view.getCardDateInput());
            try {
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                aes.encryptAndSerialize(newCard,new FileOutputStream(Storage.getCardPath(AppState.getInstance().getSession().getUsername())));
                Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                            } catch (IOException er) {
                                er.printStackTrace();
                            }
                        } );
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

