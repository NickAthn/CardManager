package app.viewController;

import app.AppState;
import app.model.Card;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.view.RemoveView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.BaseStream;
import java.util.stream.Stream;


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
                AESCryptographer aes = AppState.getInstance().getUserCryptographer();
                AtomicReference<Path> cardpath = new AtomicReference<>();


                Files.walk(Paths.get(Storage.getCardsDir(AppState.getInstance().getSession().getUsername())))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                Card card = (Card) aes.decryptAndDeserialize(new FileInputStream(path.toString()));
                                if (card.getType().equals(view.getTypeInput()) && card.getNumber().equals(view.getCardNumInput())) {
                                    cardpath.set(path);
                                    showMessage("Card Removed","Success");
                                }


                            } catch (IOException er) {
                                er.printStackTrace();
                            }  catch (Exception e) {
                                e.printStackTrace();
                            }

                        } );

                Files.deleteIfExists(cardpath.get());






            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage(),"Failed to remove the card.");
            }



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
