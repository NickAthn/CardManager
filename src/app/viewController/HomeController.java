package app.viewController;

import app.AppState;
import app.Application;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;
import app.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    private final HomeView view;

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
                view.showMessage("FILES HAVE BEEN TEMPERED!","WARNING");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupListeners();
    }

    void setupListeners() {
        view.addAddListener(new AddButtonListener());
        view.addEditListener(new EditButtonListener());
        view.addDelListener(new DelButtonListener());
        view.addShowListener(new ShowButtonListener());
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
            EditView view = new EditView();
            EditViewController controller = new EditViewController(view);
            view.show();
        }
    }
    static class DelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            RemoveView view = new RemoveView();
            RemoveController controller = new RemoveController(view);
            view.show();

        }
    }
    static class ShowButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ShowView view = new ShowView();
            ShowController controller = new ShowController(view);
            view.show();
        }
    }


}