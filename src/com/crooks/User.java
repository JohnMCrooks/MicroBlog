package com.crooks;

import java.util.ArrayList;

/**
 * Created by johncrooks on 6/6/16.
 */
public class User {
    String password;
    String username;
    ArrayList<String> messageList;

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

}
