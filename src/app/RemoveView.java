package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class RemoveView {
    private JFrame frame;
    private JTextField typeField, cardnumField;
    private JButton removeButton, backButton;

    public RemoveView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        typeField = new JTextField(16);
        cardnumField = new JTextField(16);

        removeButton = new JButton("Remove");
        backButton = new JButton("Home");


        frame = new JFrame("Remove");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set constraints and add subviews
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(createRemovePanel(), gc);

    }

    private JPanel createRemovePanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Remove");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting username field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Type:"), gc);
        gc.gridx = 1;
        panel.add(typeField, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Card Number:"), gc);
        gc.gridx = 1;
        panel.add(cardnumField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(backButton, gc);
        gc.gridx = 1;
        panel.add(removeButton, gc);

        return panel;
    }

    // Listener setters
    void addRemoveListener(ActionListener listener) {
        removeButton.addActionListener(listener);
    }
    void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Value Getters
    String getTypeInput() { return typeField.getText(); }
    String getCardNumInput() { return cardnumField.getText(); }

    // View Methods
    void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void dispose() {
        frame.dispose();
    }

    public void showMessage(String message, String titleBar) {
        JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}
