package userbot;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class MainController {

    @PostMapping
    public String registerUser(@RequestBody String body, HttpSession session) throws SQLException {
        JSONObject json = new JSONObject(body);
        UserRegistrationService registrationService = new UserRegistrationService();
        UserService userService = new UserService();
        String responseMessage = "";

        // Testing if the user exists in the DB
        try {
            User user = userService.getUserByPhoneNumber(json.optString("phone", ""));
            if (user != null) {
                responseMessage = "Welcome back, " + user.getName() + "!";
                return new JSONObject().put("message", responseMessage).toString();
            } else {
                // User does not exist, so register the user
                responseMessage = registrationService.registerUser(json, session);
                return new JSONObject().put("message", responseMessage).toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new JSONObject().put("message", "An error occurred. Please try again later.").toString();
        }

    }

  /*  @PostMapping("/request")
    public String  launchRequest(@RequestBody String body,HttpSession requestsSession) {
        UserService userService = new UserService();
        JSONObject json = new JSONObject(body);

        String responseMessage = "";
        try {
            User user = userService.getUserByPhoneNumber(json.optString("phone", ""));
            if (user != null) {
                responseMessage = "Welcome back, " + user.getName() + "!";
                return new JSONObject().put("message", responseMessage).toString();
            } else {
                // User does not exist, so register the user
                return ResponseEntity.status(302) // HTTP 302 Found
                        .header("Location", "/user") // Redirect to the registration endpoint
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new JSONObject().put("message", "An error occurred. Please try again later.").toString();
        }



    }

*/



}
