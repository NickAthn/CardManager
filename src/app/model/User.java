package app.model;

import java.io.Serializable;

public class User implements Serializable {
    // Properties
    public String username; // Username must be unique and acts as and UUID
    public String password;
    public String name;
    public String email;

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
