package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.EditView;
import app.view.ShowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class EditController {
    private EditView view;


    EditController(EditView view) {
        this.view = view;

        setupListeners();
    }

    void setupListeners() {
        view.addEditListener(new EditController.EditButtonListener());
        view.addBackListener(new EditController.BackButtonListener());
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
