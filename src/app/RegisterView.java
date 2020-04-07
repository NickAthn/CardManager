package app;

import javax.swing.*;

public class RegisterView {
    private JFrame frame;

    private JTextField nameField = new JTextField(16);
    private JTextField emailField = new JTextField(16);
    private JTextField usernameField = new JTextField(16);
    private JPasswordField passwordField = new JPasswordField(16);
    private JButton registerButton = new JButton("Register");

    public RegisterView() {
        setupView();
    }

    private void setupView() {
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
