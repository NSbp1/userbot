package userbot.services;

import org.json.JSONObject;
import userbot.models.UserSession;
import java.sql.SQLException;
import userbot.models.User;

public class UserRegistrationStageHandler {

    private final UserSessionService userSessionService; // Declare UserSessionService
    private final UserService userService; // Declare UserService

    // Constructor to inject UserSessionService and UserService
    public UserRegistrationStageHandler(UserSessionService userSessionService, UserService userService) {
        this.userSessionService = userSessionService;
        this.userService = userService; // Initialize userService
    }

    public String handleGreetStage(UserSession userSession) {
        userSession.setStage("name");
        return "Hello, let's register you! What is your name?";
    }

    public String handleNameStage(UserSession userSession, JSONObject json) {
        String name = json.optString("message", "").trim();
        if (name.isEmpty()) {
            return "Please provide a valid name.";
        }
        userSession.setName(name);
        try {
            userSessionService.updateUserSession(userSession);  // Use userSessionService injected via constructor
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while updating the session. Please try again.";
        }
        userSession.setStage("surname");
        return "Okay " + name + ", what is your surname?";
    }

    public String handleSurnameStage(UserSession userSession, JSONObject json) {
        String surname = json.optString("message", "").trim();
        if (surname.isEmpty()) {
            return "Please provide a valid surname.";
        }
        userSession.setSurname(surname);
        userSession.setStage("email");
        return "Great! Now, what is your email address, " + userSession.getName() + "?";
    }

    public String handleEmailStage(UserSession userSession, JSONObject json) {
        String email = json.optString("message", "").trim();
        if (!isValidEmail(email)) {
            return "Please provide a valid email address.";
        }
        userSession.setEmail(email);
        userSession.setStage("decision");
        return "Are these details correct?\n1. Name: " + userSession.getName() + "\n" +
                "2. Surname: " + userSession.getSurname() + "\n" +
                "3. Email: " + email + "\nType 'Yes' or 'No'.";
    }

    public String handleDecisionStage(UserSession userSession, JSONObject json) throws SQLException {
        String decision = json.optString("message", "").trim();
        if (decision.equalsIgnoreCase("no")) {
            userSession.setStage("name");
            return "Enter your name.";
        } else if (decision.equalsIgnoreCase("yes")) {
            try {
                // Begin transaction or use proper transactional management
                String fullName = userSession.getName() + " " + userSession.getSurname();
                userService.createUser(new User(fullName, userSession.getEmail(), userSession.getPhoneNumber()));
                userSessionService.deleteUserSession(userSession.getId());
                return "Registration successful! Welcome, " + userSession.getName() + "!";
            } catch (SQLException e) {
                // In case of an error, do not delete the session
                e.printStackTrace(); // Optional: Print the stack trace for debugging
                return "There was an error while registering. Please try again later.";
            }
        }
        return "Please confirm with 'Yes' or 'No'.";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return email.matches(emailRegex);
    }
}
