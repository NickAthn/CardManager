package app.service;

import app.model.Card;
import app.model.User;
import app.util.PasswordUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class CardManager {
    private Storage storage;
    private Cryptographer crypto;

    public CardManager(){
        this.storage = new Storage();
        this.crypto = new Cryptographer();
    }

    public void addCard(String cardnum, String carduser, String type, String cvc, Date exp_date) {
        ArrayList<Card> cardList = new ArrayList<>();
        byte[] data = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            Card newCard = new Card(type, cardnum, carduser, cvc, exp_date);
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(newCard);
            oos.flush();
            data = bos.toByteArray();
//            Card encr_card = crypto.encrypt_aes(username, data);
            //SealedObject sealedObject = new SealedObject( newCard, cipher);
            //cardList.add(newCard);
            //storage.saveCards(cardList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

