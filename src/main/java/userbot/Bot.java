/*package userbot;

import org.json.JSONObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


import java.sql.SQLException;

public class Bot {
    private UserService userService = new UserService();
/*
    public String handleUserMessage(String message, String phoneNumber) {
        try {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (user != null) {
                return "Welcome back, " + user.getName() + "!";
            } else if (message.equalsIgnoreCase("register")) {
                return "Please provide your name and email to register.";
            }
            return "To Register provide (name, email, and phone number)";
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred. Please try again later.";
        }
    }
    */

    // the new structure starts



//}

   // the end of the new structure

    /*

    public String stage = "greet";

    public String registerUser(String message,  String phoneNumber) {
        String name,email,surname,gender;
        try {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (user != null) {
                return "Welcome back, " + user.getName() + "!";
            }else {
                while(stage != "done") {

                    switch (stage) {
                        case "greet":
                            System.out.println("Hello! What is your name?");
                            stage = "name";  // Move to next stage
                            break;

                        case "name":
                            name = message;  // Store name
                            System.out.println("Nice to meet you, " + name + "! What is your surname?");
                            stage = "surname";  // Move to next stage
                            break;

                        case "surname":
                            surname = message;  // Store name
                            System.out.println("okay, " + name + " " + surname + " may i please have your email?");
                            stage = "surname";  // Move to next stage
                            break;

                        case "email":
                            name = message;  // Store name
                            System.out.println("Nice to meet you, " + name + "! What is your surname?");
                            stage = "surname";  // Move to next stage
                            break;

                        case "gender":
                            name = message;  // Store name
                            System.out.println("Nice to meet you, " + name + "! What is your surname?");
                            stage = "surname";  // Move to next stage
                            break;

                    User user1 = new User(name, email, phoneNumber);
                  //  userService.createUser(user1);
                    return "Registration successful, Welcome " + name + "!";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred during registration.";
        }
    }
}

*/