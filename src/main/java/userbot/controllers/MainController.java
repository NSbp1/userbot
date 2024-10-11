package userbot.controllers;

import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import userbot.models.User;
import userbot.services.UserRegistrationService;
import userbot.services.UserService;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class MainController {

    private final UserService userService;
    private final UserRegistrationService registrationService;

    // Constructor to initialize services
    public MainController(UserService userService, UserRegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @PostMapping
    public String registerUser(@RequestBody String body) throws SQLException {
        JSONObject json = new JSONObject(body);
        String responseMessage;

        // Retrieve the phone number from the JSON request
        String phoneNumber = json.optString("phone", "");

        // Check if the user exists in the DB
        User user = userService.getUserByPhoneNumber(phoneNumber);
        if (user != null) {
            // User exists, return welcome message
            responseMessage = "Welcome back, " + user.getName() + "!";
        } else {
            // User does not exist, start registration process using the UserRegistrationService
            responseMessage = registrationService.registerUser(json);
        }

        return new JSONObject().put("message", responseMessage).toString();
    }
}
