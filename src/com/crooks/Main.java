package com.crooks;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static spark.Spark.staticFileLocation;

public class Main {
    static HashMap<String, User> passMap = new HashMap<String, User>();
    public static void main(String[] args) throws FileNotFoundException {
        HashMap m = new HashMap();


        HashMap<String,User> tempMap = parseFile();


        staticFileLocation("public");// Points to the CSS file


        Spark.init();
        Spark.get(
            "/",
            (request, response )-> {
                Session session = request.session();
                String username = session.attribute("username");

                if (username==null){
                    return new ModelAndView(m,"index.html");

                } else{
                    User user = passMap.get(username);
                    m.put("name", username);
                    m.put("messageList", user.messageList);
                    return new ModelAndView(m, "messages.html");
                }
            },
            new MustacheTemplateEngine()
        );

        Spark.get(
                "/post-list",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if (username==null){
                        return new ModelAndView(m,"index.html");

                    } else{
                        return new ModelAndView(m,"messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                (request, response) -> {

                    String username = request.queryParams("username");      //Take Username input -->> the String here must match the String from the m.put in the  "/" route
                    String password = request.queryParams("password");      //Take Password input
                    if(username==null || password==null){
                        return new ModelAndView(m, "login.html");
                    }else {
                        User user = passMap.get(username);
                        if (user==null){
                            user=new User(password,username);
                            passMap.put(username, user);
                        }else if(!password.equals(user.password)){
                            Spark.halt("Incorrect Password");
                        }

                    }
                    Session session = request.session();
                    session.attribute("username", username);

                    jsonExport(passMap);

                    response.redirect("/    ");                   // direct to next page
                    return "";
                });

        Spark.post(
                "/create-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if(username==null){
                        throw new Exception("Not Logged in");
                    }

                    User user = passMap.get(username);
                    Message m1 = new Message(request.queryParams("msgcontents"));
                    user.messageList.add(m1);


                    jsonExport(passMap);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/delete-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if(username==null){
                        throw new Exception("Not Logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));
                    User user = passMap.get(username);
                    if(id<=0|| id-1>= user.messageList.size()){
                        Spark.halt("Error: Use a valid number");
                    }
                    user.messageList.remove(id-1);

                    jsonExport(passMap);

                    response.redirect("/");
                    return"";

                }

        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if(username==null){
                        throw new Exception("Not Logged in");
                    }
                    int id2 = Integer.valueOf(request.queryParams("id2"));
                    User user = passMap.get(username);

                    if(id2<=0|| id2-1>= user.messageList.size()){
                        Spark.halt("Error: Use a valid number");
                    }

                    Message m1 = new Message(request.queryParams("newMessage"));
                    user.messageList.set(id2, m1);

                    jsonExport(passMap);

                    response.redirect("/");
                    return "";

                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");                                //Redirect the user back to the start page
                    return "";
                }
        );

    }  //End of main method

    public static void jsonExport(HashMap<String,User> passMap) throws IOException {
        String filename = "User_message.json";
        JsonSerializer serializer = new JsonSerializer();
        HashMap<String,User> filetext = passMap;
        String json = serializer.include("*").serialize(filetext);
        File f = new File(filename);
        FileWriter fw = new FileWriter(f);
        fw.write(json);
        fw.close();
    }

    public static HashMap<String, User> parseFile() throws FileNotFoundException {
        File f = new File("User_message.json");
        Scanner scanner = new Scanner(f);
        scanner.useDelimiter("\\Z");
        String contents = scanner.next();
        JsonParser parser = new JsonParser();

        HashMap<String, User> temp = parser.parse(contents, HashMap.class);
        return temp;

        }
}   //End of Main Class
