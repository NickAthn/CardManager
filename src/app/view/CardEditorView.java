package app.view;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class CardEditorView implements View {

    private JFrame frame;
    private JTextField cardnumberField, carduserField;
    private JTextField cvcField, typeField;
    private JButton AddButton, backButton;
    private UtilDateModel model;
    private Properties p;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;

    public CardEditorView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {
        // Initializing Propeties/Views
        cardnumberField = new JTextField(16);
        carduserField = new JTextField(16);
        cvcField = new JTextField(16);
        typeField = new JTextField(16);

        model = new UtilDateModel();
        p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new CardEditorView.DateLabelFormatter());

        AddButton = new JButton("Add");
        backButton = new JButton("Home");


        frame = new JFrame("Add");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        panel.add(datePicker, gc);

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

    public void addAddListener(ActionListener listener) {
        AddButton.addActionListener(listener);
    }
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Value Getters
    public String getCardNumInput() { return cardnumberField.getText(); }
    public String getCardUserInput() { return carduserField.getText(); }
    public String getCardTypeInput() { return typeField.getText(); }
    public String getCardCvcInput() { return cvcField.getText(); }
    public Date getCardDateInput() { return (Date) datePicker.getModel().getValue(); }

    //Setters
    public void setCardnumberField(String cardnum){ cardnumberField.setText(cardnum);}
    public void setCarduserField(String cardus){ carduserField.setText(cardus);}
    public void setCardcvcField(String cardcvc){ cvcField.setText(cardcvc);}
    public void setCardtypeField(String cardtype){ typeField.setText(cardtype);}
    public void setCarddateField(String date){ datePicker.getJFormattedTextField().setText(date);}

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
