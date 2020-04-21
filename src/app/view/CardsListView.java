package app.view;

import javax.swing.*;

public class CardsListView implements View {
    private JFrame frame;
    private JButton AddButton, ShowButton, EditButton, DelButton, LogButton;

    public CardsListView() {
        setupComponents();
    }

    // View UI Construction
    private void setupComponents() {

    }


    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void dispose() {
        frame.dispose();
    }
}
