package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class CardView {

    private JFrame frame;
    private JTextField cardnumberField, carduserField;
    private JPasswordField cvcField;
    private JButton AddButton, backButton;

    public CardView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        cardnumberField = new JTextField(16);
        carduserField = new JTextField(16);
        cvcField = new JPasswordField(16);

        AddButton = new JButton("Add");
        backButton = new JButton("Home");


        frame = new JFrame("Add");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set constraints and add subviews
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(createAddPanel(), gc);
        //gc.gridy++;

    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Add");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting username field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Card Number:"), gc);
        gc.gridx = 1;
        panel.add(cardnumberField, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Card User:"), gc);
        gc.gridx = 1;
        panel.add(carduserField, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(new JLabel("Date Exp. :"), gc);
        gc.gridx = 1;
        panel.add(carduserField, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 3;
        panel.add(new JLabel("CVC:"), gc);
        gc.gridx = 1;
        panel.add(cvcField, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 4;
        panel.add(AddButton, gc);

        return panel;
    }

    void addAddListener(ActionListener listener) {
        AddButton.addActionListener(listener);
    }
    void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Value Getters
    //String getCardNumInput() { return usernameField.getText(); }
    //String getCardUserInput() { return passwordField.getPassword(); }



    // View Methods
    void show() {
        frame.pack();
        frame.setVisible(true);
    }

    void dispose() {
        frame.dispose();
    }
}
