package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    private HomeView view;

    HomeController(HomeView view) {
        this.view = view;
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addEditListener(new ShowButtonListener());
        view.addDelListener(new EditButtonListener());
        view.addShowListener(new DelButtonListener());
        view.addLogListener(new LogoutButtonListener());

    }

    static class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CardView view = new CardView();
            CardController controller = new CardController(view);
            view.show();
        }
    }
    class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
        }
    }
    static class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //RegisterView view = new RegisterView();
            //RegisterController controller = new RegisterController(view);
            //view.show();
        }
    }
    static class DelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //RegisterView view = new RegisterView();
            //RegisterController controller = new RegisterController(view);
            //view.show();

        }
    }
    static class ShowButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //RegisterView view = new RegisterView();
            //RegisterController controller = new RegisterController(view);
            //view.show();
        }
    }


}