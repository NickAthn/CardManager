package app.view;

import javax.swing.*;

public class CardView implements View {
    private JFrame frame;
    private JLabel cardholderField, cvc, expDate, cardNumber;


    CardView() {setupView();}

    void setupView() {
        frame = new JFrame("");

    }
}
