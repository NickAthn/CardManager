package app;

import app.model.User;
import app.service.Storage;
import app.service.security.AESCryptographer;
import app.service.security.RSACryptographer;

public class AppState {
    private RSACryptographer appCryptographer;
    private AESCryptographer userCryptographer;
    private Storage storage;

    private User activeSession;

    // Singleton Pattern
    private static final AppState instance = new AppState();
    public static AppState getInstance() {
        return instance;
    }
    private AppState(){}

    public User getSession() throws Exception {
        if (activeSession == null) throw new Exception("No active session found.");
        return activeSession;
    }

    public RSACryptographer getAppCryptographer() throws Exception {
        if (appCryptographer == null) throw new Exception("Failed to load App Cryptographer.");
        return appCryptographer;
    }
    public AESCryptographer getUserCryptographer() throws Exception {
        if (activeSession == null || userCryptographer == null) throw new Exception("Failed to load session cryptographer.");
        return userCryptographer;
    }
    public Storage getStorage() throws Exception {
        if (storage == null) throw new Exception("StorageManager is not available!");
        return storage;
    }

    public void setSession(User user) { this.activeSession = user; }
    public void setAppCryptographer(RSACryptographer rsa) { this.appCryptographer = rsa; }
    public void setUserCryptographer(AESCryptographer aes) { this.userCryptographer = aes; }
    public void setStorage(Storage storage) { this.storage = storage; }
}