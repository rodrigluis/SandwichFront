package be.abis.sandwich.model;

import org.springframework.stereotype.Component;

@Component
public class Credentials {
    private String userName;
    private String userPassword;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
