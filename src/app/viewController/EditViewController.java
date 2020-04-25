package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.CardEditorView;
import app.view.EditView;
import app.view.ShowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

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
                CardEditorView view1 = new CardEditorView();
                EditController controller = new EditController(view1);

                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                //decrypt every card
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));

                                if (card.getType().equals(view.getTypeInput()) && card.getNumber().equals(view.getCardNumInput())) {

                                    view1.setCardnumberField(card.getNumber());
                                    view1.setCardcvcField(card.getCvc());
                                    view1.setCardtypeField(card.getType());
                                    view1.setCarduserField(card.getCardholder());
                                    Date day = card.getExpirationDate();
                                    view1.setCarddateField(day.getDay(),day.getMonth(),day.getYear());
                                    view1.show();
                                }
                            } catch (IOException er) {
                                er.printStackTrace();
                            }
                        } );


            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage(),"Failed to show the cards.");
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
