package app.viewController;


import app.model.Card;
import app.service.Authenticator;
import app.service.Cryptographer;
import app.view.CardEditorView;
import app.service.Storage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public class CardEditorController {
    private CardEditorView view;
    private Storage storage;
    private Cryptographer crypto;

    public CardEditorController(CardEditorView view, Storage storage) {
        this.view = view;
        this.storage = storage;
        this.crypto = new Cryptographer();
        setupListeners();
        try {
            Files.walk(Paths.get(storage.userDir))
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addBackListener(new BackButtonListener());
    }

    public void showMessage(String message, String title) {
        view.showMessage(message, title);
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Card newCard = new Card(view.getCardTypeInput(),view.getCardNumInput(),view.getCardUserInput(),view.getCardCvcInput(),view.getCardDateInput());
            try {
                SealedObject sealed = crypto.sealObject(Authenticator.getInstance().session.getUsername(),newCard);
                crypto.saveSealedObject(Authenticator.getInstance().session.getUsername(),storage.userDir + UUID.randomUUID().toString(),sealed);
                Files.walk(Paths.get(storage.userDir))
                        .filter(Files::isRegularFile)
                        .forEach( path -> {
                            try {
                                Card card = (Card) crypto.readSealedObject(Authenticator.getInstance().session.getUsername(),path.toString());
                                System.out.println(card.getCardholder() + " // " + card.getType());
                            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        } );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class BackButtonListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}

