package userbot.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    private static final String API_URL = "https://textbelt.com/text"; // Textbelt API endpoint
    private static final String API_KEY = "textbelt"; // You may want to externalize this

    public static void main(String[] args) {
        String phoneNumber = "+27815167314"; // Recipient's phone number
        String otp = generateOTP(); // Generate your OTP
        boolean isSent = sendSMS(phoneNumber, otp);
        if (isSent) {
            System.out.println("OTP sent successfully!");
        } else {
            System.out.println("Failed to send OTP.");
        }
    }

    public static String generateOTP() {
        // Generate a 6-digit OTP
        int otp = (int) (Math.random() * 900000) + 100000; // Change to 6-digit OTP
        return String.valueOf(otp);
    }

    public static boolean sendSMS(String phoneNumber, String otp) {
        try {
            String postData = "{" +
                    "\"phone\": \"" + phoneNumber + "\"," +
                    "\"message\": \"Your OTP is: " + otp + "\"," +
                    "\"key\": \"" + API_KEY + "\"" +
                    "}";

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            return responseCode == 200; // Return true if OTP sent successfully

        } catch (Exception e) {
            e.printStackTrace(); // Consider logging this instead of printing
            return false; // Return false on failure
        }
    }
}
