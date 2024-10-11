package userbot.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import userbot.models.UserSession;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Service
public class UserRegistrationService {

    private final UserService userService;
    private final UserSessionService userSessionService;
    private final UserRegistrationStageHandler stageHandler;
    private final OtpStorage otpStorage;

    // Stage handlers map
    private final Map<String, BiFunction<UserSession, JSONObject, String>> stageHandlers;

    // Constructor-based dependency injection
    public UserRegistrationService(UserService userService, UserSessionService userSessionService,OtpStorage otpStorage) {
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.otpStorage = otpStorage;
           // Injected instance

        // Pass the required instances to the handler
        this.stageHandler = new UserRegistrationStageHandler(userSessionService, userService,otpStorage);

        // Initialize the stage handlers map
        stageHandlers = new HashMap<>();

        // Populate the map with stage handlers (lambda expressions)
        stageHandlers.put("greet", (userSession, json) -> stageHandler.handleGreetStage(userSession));
        stageHandlers.put("otp", (userSession, json) -> stageHandler.handleOtpStage(userSession, json));  // OTP stage handler
        stageHandlers.put("name", (userSession, json) -> stageHandler.handleNameStage(userSession, json));
        stageHandlers.put("surname", (userSession, json) -> stageHandler.handleSurnameStage(userSession, json));
        stageHandlers.put("email", (userSession, json) -> stageHandler.handleEmailStage(userSession, json));
        stageHandlers.put("decision", (userSession, json) -> {
            try {
                return stageHandler.handleDecisionStage(userSession, json);
            } catch (SQLException e) {
                return "Error processing registration.";
            }
        });
    }


    public String registerUser(JSONObject json) throws SQLException {
        String phoneNumber = json.optString("phone", "");
        UserSession userSession = userSessionService.getUserSessionByPhoneNumber(phoneNumber);

        // Create a new session if not found
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setPhoneNumber(phoneNumber);
            userSession.setStage("greet");  // Start at the 'greet' stage
            userSessionService.createUserSession(userSession); // Persist new session
        }

        // Get the current stage
        String stage = userSession.getStage();

        // Look up the handler for the current stage
        BiFunction<UserSession, JSONObject, String> stageHandler = stageHandlers.get(stage);

        if (stageHandler != null) {
            // Call the handler for the current stage
            String responseMessage = stageHandler.apply(userSession, json);

            // Update the session after processing (persists the changes)
            userSessionService.updateUserSession(userSession);

            return responseMessage;
        } else {
            return "Invalid stage or something went wrong.";
        }
    }
}
