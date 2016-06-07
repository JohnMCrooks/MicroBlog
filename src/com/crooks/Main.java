package com.crooks;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.staticFileLocation;

public class Main {
    static User user;
    static Message m1;
    static ArrayList<User> userList = new ArrayList<>();
    static ArrayList<Message> msgArray = new ArrayList<>();
    public static void main(String[] args) {
        HashMap m = new HashMap();
        HashMap<String, User> passMap = new HashMap<String, User>();
        staticFileLocation("/public");
        Spark.init();
        Spark.get(
            "/",
            (request, response )-> {

                if (user==null){
                    return new ModelAndView(m,"index.html");

                } else{

                    m.put("name", user.username);
                    m.put("password",user.password);
                    m.put("messages",msgArray);
                    m.put("msgContent", m1);
                    return new ModelAndView(m, "messages.html");
                }
            },
            new MustacheTemplateEngine()
        );
        Spark.get(
                "/post-list",
                (request, response) -> {
                return new ModelAndView(m,"messages.html");
                },
                new MustacheTemplateEngine());


        Spark.post(
                "/create-user",
                (request, response) -> {

                    String username = request.queryParams("username");      //Take Username input
                    String password = request.queryParams("password");      //Take Password input

                    if(passMap.containsKey(username)){
                        if(passMap.get(username).password.equals(password)){
                            response.redirect("/post-list");                  // Continue along to the messages page...
                            return "";
                        }else{
                            Spark.halt("Wrong password Bro, Go back and try again.");
                        }

                    }else{
                        user = new User(password, username);           //Create new user with new input
                       // userList.add(user);                            //Add user to the ArrayList of Users
                        passMap.put(user.username, user);              //And to the hashmap for future reference.
                        m.put("name", user.username);
                    }

                    response.redirect("/post-list");
                    return "";
                });

        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("msgcontents");   //Requesting the input from html, the two names must coincide or this WILL FAIL
                    m1 = new Message(message);                             //Create a new Message Object passing the Users input. trying to set m1.msgContents = message gave a Null Exception Error
                    msgArray.add(m1);                                      // adding the new Message object containing the UI to the Array list
                    response.redirect("/");                                //refresh to the main page.
                    return "";
                }
        );

        Spark.post(
                "/logout",
                (request, response) -> {
                    user = null;                                           //Set user value back to null
                    msgArray = new ArrayList<Message>();                   //resetting the message Array for New User
                    response.redirect("/");                                //Redirect the user back to the start page
                    return "";
                }
        );

    }  //End of main method
}   //End of Main Class
