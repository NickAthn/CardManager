package app.view;

import javax.swing.*;

/*
* This interface declares that a class has a method to display an alert to the user.
* This way our classes can show alerts in an abstract way with out having to know where the alerts
* are going to be displayed.
*/
public interface View {
    JFrame frame = new JFrame();

    default public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    default public void dispose() {
        frame.dispose();
    }
}
