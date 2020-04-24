package app.viewController;

import app.AppState;
import app.model.Card;
import app.model.User;
import app.service.Storage;
import app.view.RemoveView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                if (storage.readCard(view.getTypeInput()) == null && view.getCardNumInput() == null){
                    showMessage("Failed.","Card does not exist.");
                }
                else{
                    storage.deleteCard(view.getTypeInput(), view.getCardNumInput());
                    showMessage("Success.","Card removed.");
                }


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
