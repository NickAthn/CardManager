package app.viewController;

import app.service.Authenticator;
import app.service.Storage;
import app.view.CardEditorView;
import app.view.HomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    private HomeView view;
    private Storage storage;

    public HomeController(HomeView view) {
        this.view = view;
        this.storage = new Storage(Authenticator.getInstance().session);
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addEditListener(new ShowButtonListener());
        view.addDelListener(new EditButtonListener());
        view.addShowListener(new DelButtonListener());
        view.addLogListener(new LogoutButtonListener());

    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CardEditorView view = new CardEditorView();
            CardEditorController controller = new CardEditorController(view,storage);
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

        }
    }


}