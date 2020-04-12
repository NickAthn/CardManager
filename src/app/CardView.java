package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class CardView {

    private JFrame frame;
    private JTextField cardnumberField, carduserField;
    private JTextField cvcField, typeField;
    private JButton AddButton, backButton;

    public CardView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        cardnumberField = new JTextField(16);
        carduserField = new JTextField(16);
        cvcField = new JTextField(16);
        typeField = new JTextField(16);

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


    }

    //function which is able to present correctly the selected Date
    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "MM-yyyy";
        private SimpleDateFormat dateFormatter;

        private DateLabelFormatter() {
            this.dateFormatter = new SimpleDateFormat(this.datePattern);
        }

        public Object stringToValue(String text) throws ParseException {
            return this.dateFormatter.parseObject(text);
        }

        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar)value;
                return this.dateFormatter.format(cal.getTime());
            } else {
                return "";
            }
        }
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Add");
        panel.setBorder(border);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);

        // Setting card number field and label
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Card Number:"), gc);
        gc.gridx = 1;
        panel.add(cardnumberField, gc);

        // Setting card user field and label
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Card User:"), gc);
        gc.gridx = 1;
        panel.add(carduserField, gc);

        // Setting date field and label
        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(new JLabel("Date Exp. :"), gc);
        gc.gridx = 1;
/*
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new CardView.DateLabelFormatter());*/
        //panel.add(datePicker, gc);

        // Setting cvc field and label
        gc.gridx = 0;
        gc.gridy = 3;
        panel.add(new JLabel("CVC:"), gc);
        gc.gridx = 1;
        panel.add(cvcField, gc);

        // Setting type field and label
        gc.gridx = 0;
        gc.gridy = 4;
        panel.add(new JLabel("Type:"), gc);
        gc.gridx = 1;
        panel.add(typeField, gc);

        //Setting
        gc.gridx = 1;
        gc.gridy = 5;
        panel.add(AddButton, gc);
        gc.gridx = 0;
        panel.add(backButton, gc);


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
