package app.service;

public class Router {
    // Declaring global instance (singleton)
    private static final Router instance = new Router();
    private Router(){}
    public static Router getInstance() {
        return instance;
    }

    ViewController activeVC;

    void present(ViewController vc) {
        vc.show();
    }
    void push(ViewController vc) {
        activeVC.dismish();
        vc.show();
    }
}
