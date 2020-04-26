package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.CardEditorView;
import app.view.EditView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditViewController {
    private EditView view;


    EditViewController(EditView view) {
        this.view = view;

        setupListeners();
    }

    void setupListeners() {
        view.addEditListener(new EditViewController.EditButtonListener());
        view.addBackListener(new EditViewController.BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try{
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                //decrypt every card
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                //if card type and number inputs are equals with card's elements in card directory
                                //set the values in the fields
                                if (card.getType().equals(view.getTypeInput()) && card.getNumber().equals(view.getCardNumInput())) {
                                    CardEditorView view = new CardEditorView();
                                    CardEditorController controller = new CardEditorController(card, view);
                                    view.show();
                                }
                            } catch (IOException er) {
                                er.printStackTrace();
                            }
                        } );
            } catch (Exception e) {
                e.printStackTrace();
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
