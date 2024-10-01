package userbot.controllers;

import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import userbot.models.User;
import userbot.models.UserSession;

import userbot.services.UserSessionService;
import userbot.services.UserRegistrationService;
import userbot.services.UserService;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class MainController {

    private final UserService userService;
    private final UserRegistrationService registrationService;
     // Service for session handling
    private final UserSessionService userSessionService;  // Injecting UserSessionService

    // Constructor to initialize services
    public MainController(UserService userService, UserRegistrationService registrationService, UserSessionService userSessionService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.userSessionService = userSessionService;  // Initializing UserSessionService
    }

    @PostMapping
    public String registerUser(@RequestBody String body) throws SQLException {
        JSONObject json = new JSONObject(body);
        String responseMessage;

        // Retrieve the UserSession using phone number or identifier from the SessionService
        String phoneNumber = json.optString("phoneNumber");

        // Get the session or create a new one
        UserSession userSession = userSessionService.getUserSessionByPhoneNumber(phoneNumber);
        if (userSession == null) {
            userSession = new UserSession(phoneNumber);  // Create a new session
            userSession.setStage("greet");  // Set the initial stage
            userSessionService.createUserSession(userSession);  // Save it
        } else {
            // Update session if needed based on user interactions
            userSession.setStage("newStage");  // Update stage based on interaction
            userSessionService.updateUserSession(userSession);  // Save changes
        }

        // Check if the user exists in the DB
        try {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (user != null) {
                // User exists, return welcome message
                responseMessage = "Welcome back, " + user.getName() + "!";
            } else {
                // User does not exist, start registration process using the UserSession
                responseMessage = registrationService.registerUser(json, userSession);
                // Update session after registration
                userSessionService.updateUserSession(userSession);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new JSONObject().put("message", "An error occurred while accessing the database. Please try again later.").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("message", "An unexpected error occurred. Please try again later.").toString();
        }

        return new JSONObject().put("message", responseMessage).toString();
    }
}
