package ict.bean;
import java.io.Serializable;

public class UserInfo implements Serializable {
    private String username;
    private String password;

    public UserInfo() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
