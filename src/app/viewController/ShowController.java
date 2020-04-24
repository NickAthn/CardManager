package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.ShowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowController {
    private ShowView view;


    ShowController(ShowView view) {
        this.view = view;

        setupListeners();
    }

    void setupListeners() {
        view.addShowListener(new ShowButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }
    // Button Listeners
    class ShowButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try{
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                //counter
                AtomicInteger row = new AtomicInteger();
                //search the path for the cards
                    Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                            .filter(Files::isRegularFile)
                            .forEach( path -> {
                                try {
                                    //decrypt every card
                                    Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                    //if the type input is equals with the card type , add the card in the table
                                    if (card.getType().equals(view.getTypeInput())) {
                                        view.listModel.addRow(new Object[0]);
                                        view.listModel.setValueAt(card.getNumber(), row.get(), 0);
                                        view.listModel.setValueAt(card.getCardholder(), row.get(), 1);
                                        view.listModel.setValueAt(card.getType(), row.get(), 2);
                                        view.listModel.setValueAt(card.getExpirationDate(), row.get(), 3);
                                        view.listModel.setValueAt(card.getCvc(), row.get(), 4);
                                        //counter++
                                        row.getAndIncrement();
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
