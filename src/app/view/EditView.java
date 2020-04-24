package app.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditView implements View {
    private JFrame frame;
    private JTextField typeField, cardnumField;
    private JButton editButton, backButton;

    public EditView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        typeField = new JTextField(16);
        cardnumField = new JTextField(16);

        editButton = new JButton("Edit");
        backButton = new JButton("Home");


        frame = new JFrame("Edit");
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

        Border border = BorderFactory.createTitledBorder("Edit");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting type field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Type:"), gc);
        gc.gridx = 1;
        panel.add(typeField, gc);

        // Setting cardnumber field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Card Number:"), gc);
        gc.gridx = 1;
        panel.add(cardnumField, gc);

        // Setting buttons
        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(backButton, gc);
        gc.gridx = 1;
        panel.add(editButton, gc);

        return panel;
    }

    // Listener setters
    public void addEditListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Value Getters
    public String getTypeInput() { return typeField.getText(); }
    public String getCardNumInput() { return cardnumField.getText(); }

    // View Methods
    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void dispose() {
        frame.dispose();
    }

    public void showMessage(String message, String titleBar) {
        JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}
