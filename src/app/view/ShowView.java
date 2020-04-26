package app.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;



public class ShowView implements View {
    private JFrame frame;
    private JTextField typeField;
    private JButton showButton, backButton;
    public JTable table;


    public ShowView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        typeField = new JTextField(16);

        showButton = new JButton("Show");
        backButton = new JButton("Home");

        String[] columnNames = { "Card Number", "Cardholder", "Type", "Exp. Date", "CVC" };
        String[][] emptyData = {};
        table = new JTable(new DefaultTableModel(emptyData,columnNames));
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.setDefaultEditor(Object.class, null);

        frame = new JFrame("Show");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500,200);

        // Set constraints and add subviews
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(10, 10, 10, 10);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        frame.add(createShowPanel(), gc);
    }

    private JPanel createShowPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Show");
        panel.setBorder(border);


        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);

        // Setting type field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Type:"), gc);
        gc.gridx = 1;
        panel.add(typeField, gc);

        // Setting buttons
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(backButton, gc);
        gc.gridx = 1;
        panel.add(showButton, gc);

        // Setting listview
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridwidth = 2;
        panel.add(new JScrollPane(table), gc);

        return panel;
    }

    // Listener setters
    public void addShowListener(ActionListener listener) {
        showButton.addActionListener(listener);
    }
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Value Getters
    public String getTypeInput() { return typeField.getText(); }

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
