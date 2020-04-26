package app.model;

import java.io.Serializable;

public class User implements Serializable {
    // Properties
    private String username; // Username must be unique and acts as UUID
    private String password;
    private String name;
    private String email;

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUsername() { return username;}
    public String getPassword() { return password;}
    public String getName() { return name;}
    public String getEmail() { return email;}

    public void setUsername(String username) { this.username = username;}
    public void setPassword(String password) { this.password = password;}
    public void setName(String name) { this.name = name;}
    public void setEmail(String email) { this.email = email;}

}
