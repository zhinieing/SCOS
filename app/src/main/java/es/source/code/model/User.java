package es.source.code.model;

import java.io.Serializable;

/**
 * Created by pengming on 2017/10/13.
 */

public class User implements Serializable{
    private String userName;
    private String password;
    private Boolean oldUser;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOldUser() {
        return oldUser;
    }

    public void setOldUser(Boolean oldUser) {
        this.oldUser = oldUser;
    }
}
