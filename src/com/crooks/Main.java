package com.crooks;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response )-> {
                    HashMap m = new HashMap();
                    if (user==null){
                        return new ModelAndView(m,"index.html");

                    } else{
                        m.put("name", user.username);
                        m.put("password",user.password);
                        m.put("messages", user.messageList);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    user = new User(password,username);
                    userList.add(user);
                    response.redirect("/");
                    return "";


                }
        );

        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("message");
                    user.messageList.add(message);
                    response.redirect("messages.html");
                    return "";
                }
        );









    }  //End of main method
}   //End of Main Class
