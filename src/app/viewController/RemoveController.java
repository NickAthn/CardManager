package app.viewController;

import app.AppState;
import app.model.Card;
import app.model.User;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.util.FileUtils;
import app.view.RemoveView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static app.service.Storage.getCardPath;


public class RemoveController {
    private RemoveView view;


    RemoveController(RemoveView view) {
        this.view = view;

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
            try{
                Storage storage = AppState.getInstance().getStorage();
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                Files.walk(Paths.get(getCardPath(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                ArrayList<Card> allCards = (ArrayList<Card>) FileUtils.readObject(getCardPath(AppState.getInstance().getSession().getUsername())) ;
                                System.out.println(allCards);
                                for (Card card1 : allCards) {
                                    if (card1.getType().equals(view.getTypeInput()) && card1.getNumber().equals(view.getCardNumInput())) {
                                        allCards.remove(card);
                                    }
                                }
                                for (Card card2 : allCards) {
                                    aes.encryptAndSerialize(card2, new FileOutputStream(Storage.getCardPath(AppState.getInstance().getSession().getUsername())));
                                }
                            } catch (IOException er) {
                                er.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } );
                showMessage("Card removed.","Success");




            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage(),"Failed to remove the card.");
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
