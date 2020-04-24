package app.viewController;

import app.AppState;
import app.model.Card;
import app.model.User;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.RemoveView;
import app.view.ShowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                Storage storage = AppState.getInstance().getStorage();
               /* if (storage.readCard(view.getTypeInput()) == null ){
                    showMessage("Failed.","Card does not exist.");
                }*/
                //else{
                    Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                            .filter(Files::isRegularFile)
                            .forEach( path -> {
                                try {
                                    Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                    if(card.getType().equals(view.getTypeInput())){
                                        view.listModel.addElement(card);
                                    }
                                } catch (IOException er) {
                                    er.printStackTrace();
                                }
                            } );
                //}
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
