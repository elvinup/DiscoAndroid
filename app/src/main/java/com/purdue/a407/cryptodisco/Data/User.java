package com.purdue.a407.cryptodisco.Data;

/**
 * Created by Kenny on 2/14/2018.
 */

/*
    User class

 */
public class User {

    private String UID;
    private String userName;
    private String passWord;

    // constructor for UID only
    public User (String UID) {
        this.UID = UID;
    }

    // constructor for users who decide to create an userName and passWord
    // not sure if we are still allowing users to create an account
    public User (String UID, String userName, String passWord) {
        this.UID = UID;
        this.userName = userName;
        this.passWord = passWord;
    }

    // getter
    public String getUID() {
        return UID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    // setter
    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
