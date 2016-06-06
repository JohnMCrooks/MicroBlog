package com.crooks;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static Message m1;
    static ArrayList<User> userList = new ArrayList<>();
    static ArrayList<Message> msgArray = new ArrayList<>();

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
                        m.put("messages", msgArray.toString());
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
                    msgArray.add(m1);
                    response.redirect("/");
                    return "";
                }
        );

    }  //End of main method
}   //End of Main Class
