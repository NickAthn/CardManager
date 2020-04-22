package app.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeView implements View {
    private JFrame frame;
    private JButton AddButton, ShowButton, EditButton, DelButton, LogButton;

    public HomeView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {

        AddButton = new JButton("Add Card");
        ShowButton = new JButton("Show Card");
        EditButton = new JButton("Edit Card");
        DelButton = new JButton("Delete Card");
        LogButton = new JButton("Logout");


        frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set constraints and add subviews
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(createAddPanel(), gc);


    }



    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Home");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting username field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(AddButton, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(ShowButton, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(EditButton, gc);

        // Setting password field and label
        gc.gridx = 0;
        gc.gridy = 3;
        panel.add(DelButton, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        panel.add(LogButton, gc);


        return panel;
    }

    public void addAddListener(ActionListener listener) {
        AddButton.addActionListener(listener);
    }
    public void addEditListener(ActionListener listener) {
        EditButton.addActionListener(listener);
    }
    public void addDelListener(ActionListener listener) {
        DelButton.addActionListener(listener);
    }
    public void addShowListener(ActionListener listener) {
        ShowButton.addActionListener(listener);
    }
    public void addLogListener(ActionListener listener) {
        LogButton.addActionListener(listener);
    }


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
        JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.WARNING_MESSAGE);
    }

}

