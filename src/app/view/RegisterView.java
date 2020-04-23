package app.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView implements View {
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
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.EAST;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        frame.add(new JLabel("Name:"), gc);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        frame.add(nameField, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        frame.add(new JLabel("Email:"), gc);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        frame.add(emailField, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        frame.add(new JLabel("Username:"), gc);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        frame.add(usernameField, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        frame.add(new JLabel("Master Password:"), gc);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        frame.add(passwordField, gc);

        gc.gridy++;
        gc.gridx = 0;
        frame.add(cancelButton, gc);
        gc.gridx = 1;
        frame.add(registerButton, gc);
    }
    // Listener setters
    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Value Getters
    public String getNameInput() { return nameField.getText(); }
    public String getEmailInput() { return  emailField.getText(); }
    public String getUsernameInput() { return usernameField.getText(); }
    public char[] getPasswordInput() { return passwordField.getPassword(); }

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
