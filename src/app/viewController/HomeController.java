package app.viewController;

import app.AppState;
import app.Application;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;
import app.view.CardEditorView;
import app.view.HomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    private HomeView view;

    public HomeController(HomeView view) {
        this.view = view;
        try {
            Storage storage = AppState.getInstance().getStorage();
            RSACryptographer rsa = AppState.getInstance().getAppCryptographer();
            byte[] encryptedKeyBytes = storage.readUserKeyBytes(AppState.getInstance().getSession().getUsername());
            byte[] keyBytes = rsa.decrypt(encryptedKeyBytes, RSACryptographer.USE_PRIVATE_KEY);
            AESCryptographer userCrypto = new AESCryptographer(keyBytes);
            AppState.getInstance().setUserCryptographer(userCrypto);
            if (!Application.integrityCheck()) {
                System.out.println("FILES HAVE BEEN TEMPERED WITH");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            CardEditorController controller = new CardEditorController(view);
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