package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.RemoveView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;


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
            AtomicBoolean isFound = new AtomicBoolean(false);
            try {
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                System.out.println(card.getNumber());
                                if (card.getType().equals(view.getTypeInput()) && card.getNumber().equals(view.getCardNumInput())) {
                                    Files.deleteIfExists(path);
                                    isFound.set(true);
                                    showMessage("Card removed.","Successful!");
                                }
                            } catch (Exception er) {
                                er.printStackTrace();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!isFound.get()) showMessage("Failed to remove the card.", "Failed");
        }
     }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try{


            //delete all the cards
                /*Files.walkFileTree(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        Files.delete(file); // this will work because it's always a File
                        return FileVisitResult.CONTINUE;
                    }
                });

                //save the encrypted new cards in the directory
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                for(Card card : allCards) {
                    aes.encryptAndSerialize(card, new FileOutputStream(Storage.getCardPath(AppState.getInstance().getSession().getUsername())));
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }

            view.dispose();
        }
    }


}
