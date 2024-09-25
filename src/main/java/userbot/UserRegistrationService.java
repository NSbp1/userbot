package userbot;

import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import java.sql.SQLException;

public class UserRegistrationService {

    public String registerUser(JSONObject json, HttpSession session) throws SQLException {
        // Retrieve current session attributes
        String name = (String) session.getAttribute("name");
        String surname = (String) session.getAttribute("surname");
        String email = (String) session.getAttribute("email");
        String phoneNumber = (String) session.getAttribute("phone");
        phoneNumber = json.optString("phone", "");
        String responseMessage = "";
        UserService userService = new UserService();



        // Retrieving the stage or initialize to "greet"
        String stage = (String) session.getAttribute("stage");
        if (stage == null) {
            stage = "greet";
        }

        // Process based on the current stage
        switch (stage) {
            case "greet":
                session.setAttribute("phone", phoneNumber);
                session.setAttribute("stage", "name");
                responseMessage = "Hello, let's register you! What is your name?";
                break;

            case "name":
                name = json.optString("message", "").trim();
                session.setAttribute("name", name);
                session.setAttribute("stage", "surname");
                responseMessage = "Okay " + name + ", what is your surname?";
                break;

            case "surname":
                surname = json.optString("message", "").trim();
                session.setAttribute("surname", surname);
                session.setAttribute("stage", "email");
                responseMessage = "Okay " + name + ", what is your email?";
                break;

            case "email":
                email = json.optString("message", "").trim();
                session.setAttribute("email", email);
                session.setAttribute("stage", "decision");
                responseMessage = "Okay " + name + ", are these details right?\n" +
                        "1. Name: " + name + "\n" +
                        "2. Surname: " + surname + "\n" +
                        "3. Email: " + email + "\n" +
                        "If they are correct, type Yes; if not, type No.";
                break;

            //checking if the details entered are correct else restart the registration process
            case "decision":
                String decision = json.optString("message", "").trim();
                if (decision.equalsIgnoreCase("no")) {
                    session.setAttribute("stage", "greet");
                    responseMessage = "Let's register you! Type hey.";
                } else {
                    if (name == null || surname == null || email == null ||
                            name.isEmpty() || surname.isEmpty() || email.isEmpty()) {
                        responseMessage = "Please provide all required details before submitting.";
                    } else {
                        String fullName = name + " " + surname;
                        User user1 = new User(fullName, email, phoneNumber);

                        userService.createUser(user1);
                        session.invalidate();
                        responseMessage = "Registration successful, Welcome " + name + "!";

                    }
                }
                break;

            default:
                responseMessage = "Something went wrong.";
                break;
        }

        return responseMessage;
    }
}
