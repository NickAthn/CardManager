package app.service;

/*
* This interface declares that a class has a method to display an alert to the user.
* This way our classes can show alerts in an abstract way with out having to know where the alerts
* are going to be displayed.
*/
public interface View {
    public void showMessage(String message, String title);
}
