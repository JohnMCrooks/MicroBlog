package com.crooks;

import java.util.ArrayList;

/**
 * Created by johncrooks on 6/6/16.
 */
public class User {
    String password;
    String username;
    ArrayList<Message> messageList = new ArrayList<>();

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", messageList=" + messageList +
                '}';
    }
}
