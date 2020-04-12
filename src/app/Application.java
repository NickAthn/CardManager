package app;

import javax.swing.text.View;

public class Application {
    public static void main(String[] args){
        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.show();
    }
}
