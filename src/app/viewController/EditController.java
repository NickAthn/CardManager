package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.CardEditorView;
import app.view.EditView;
import app.view.ShowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class EditController {
    private CardEditorView view;


    EditController(CardEditorView view) {
        this.view = view;
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new EditController.EditButtonListener());
        view.addBackListener(new EditController.BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Card newCard = new Card(view.getCardTypeInput(),view.getCardNumInput(),view.getCardUserInput(),view.getCardCvcInput(),view.getCardDateInput());
                    AESCryptographer aes = AppState.getInstance().getUserCryptographer();

                    Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                            .filter(Files::isRegularFile)
                            .forEach( path -> {
                                try {
                                    Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                    //aes.encryptAndSerialize(newCard,new FileOutputStream(Storage.getCardPath(AppState.getInstance().getSession().getUsername())));
                                } catch (IOException er) {
                                    er.printStackTrace();
                                }
                            } );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

        }
    }
    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }

}
