package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        usernameField = new JTextField(16);
        passwordField = new JPasswordField(16);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register now");


        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set constraints and add subviews
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(createLoginPanel(), gc);
        gc.gridy++;
        frame.add(createRegisterPanel(), gc);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Login");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting username field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Username:"), gc);
        gc.gridx = 1;
        panel.add(usernameField, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Password:"), gc);
        gc.gridx = 1;
        panel.add(passwordField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        panel.add(loginButton, gc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout());

        Border border = BorderFactory.createTitledBorder("Not registered ?");
        panel.setBorder(border);

        panel.add(registerButton);
        return panel;
    }
    // Listener setters
    void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
    void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Value Getters
    String getUsernameInput() { return usernameField.getText(); }
    char[] getPasswordInput() { return passwordField.getPassword(); }

    // View Methods
    void show() {
        frame.pack();
        frame.setVisible(true);
    }

    void dispose() {
        frame.dispose();
    }

    public void showMessage(String message, String titleBar) {
        JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}