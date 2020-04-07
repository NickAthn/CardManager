package app;

import javax.swing.*;
import java.awt.*;

public class RegisterView {
    private JFrame frame;

    private JTextField nameField = new JTextField(16);
    private JTextField emailField = new JTextField(16);
    private JTextField usernameField = new JTextField(16);
    private JPasswordField passwordField = new JPasswordField(16);
    private JButton registerButton = new JButton("Register");
    private JButton cancelButton = new JButton("Cancel");


    public RegisterView() {
        setupView();
    }

    private void setupView() {
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 5, 5, 5);

        gc.gridwidth = 2;
        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(new JLabel("Name"), gc);
        gc.gridy++;
        frame.add(nameField, gc);

        gc.gridy++;
        frame.add(new JLabel("Email"), gc);
        gc.gridy++;
        frame.add(emailField, gc);

        gc.gridy++;
        frame.add(new JLabel("Username"), gc);
        gc.gridy++;
        frame.add(usernameField, gc);

        gc.gridy++;
        frame.add(new JLabel("Master Password"), gc);
        gc.gridy++;
        frame.add(passwordField, gc);

        gc.gridwidth = 1;
        gc.gridy++;
        frame.add(cancelButton, gc);
        gc.gridx++;
        frame.add(registerButton, gc);
    }

    // View Methods
    void show() {
        frame.pack();
        frame.setVisible(true);
    }

    void dispose() {
        frame.dispose();
    }
}
