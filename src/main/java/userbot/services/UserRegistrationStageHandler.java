package userbot.services;

import org.json.JSONObject;
import userbot.models.UserSession;
import userbot.models.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserRegistrationStageHandler {

    private final UserSessionService userSessionService;
    private final UserService userService;
    private final OtpStorage otpStorage;

    public UserRegistrationStageHandler(UserSessionService userSessionService, UserService userService, OtpStorage otpStorage) {
        this.userSessionService = userSessionService;
        this.userService = userService;
        this.otpStorage = otpStorage;
    }

    // Handle greeting stage and generate OTP
    public String handleGreetStage(UserSession userSession) {
        String otp = generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes
        otpStorage.storeOtp(userSession.getPhoneNumber(), otp, expirationTime);
        userSession.setStage("otp");
        System.out.println("Generated OTP: " + otp); // In practice, send via SMS or email
        return "An OTP has been generated and printed to the console. Please enter it to continue.";
    }

    // Handle OTP stage
    public String handleOtpStage(UserSession userSession, JSONObject json) {
        String enteredOtp = json.optString("message", "").trim();
        if (otpStorage.verifyOtp(userSession.getPhoneNumber(), enteredOtp)) {
            userSession.setStage("name");
            return "OTP verified! What is your name?";
        }
        return "Invalid OTP or OTP has expired. Please try again.";
    }

    // Handle Name stage
    public String handleNameStage(UserSession userSession, JSONObject json) {
        String name = json.optString("message", "").trim();
        if (name.isEmpty()) {
            return "Please provide a valid name.";
        }
        userSession.setName(name);
        userSession.setStage("surname");
        return "Okay " + name + ", what is your surname?";
    }

    // Handle Surname stage
    public String handleSurnameStage(UserSession userSession, JSONObject json) {
        String surname = json.optString("message", "").trim();
        if (surname.isEmpty()) {
            return "Please provide a valid surname.";
        }
        userSession.setSurname(surname);
        userSession.setStage("email");
        return "Great! Now, what is your email address, " + userSession.getName() + "?";
    }

    // Handle Email stage
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

    // Handle Decision stage
    public String handleDecisionStage(UserSession userSession, JSONObject json) throws SQLException {
        String decision = json.optString("message", "").trim();

        if (decision.equalsIgnoreCase("no")) {
            userSession.setStage("name");
            return "Enter your name.";
        } else if (decision.equalsIgnoreCase("yes")) {
            String fullName = userSession.getName() + " " + userSession.getSurname();
            User newUser = new User(fullName, userSession.getEmail(), userSession.getPhoneNumber());
            userService.createUser(newUser);
            userSessionService.deleteUserSession(userSession.getId());
            return "Registration successful! Welcome, " + userSession.getName() + "!";
        }

        return "Please confirm with 'Yes' or 'No'.";
    }

    // Utility functions
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return email.matches(emailRegex);
    }

    private String generateOtp() {
        int otp = new Random().nextInt(999999);
        return String.format("%06d", otp);
    }
}
