package userbot.services;
import org.springframework.stereotype.Service;
import userbot.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpStorage {


    // Store OTP in the database
    void storeOtp(String phoneNumber, String otp, LocalDateTime expirationTime) {
        String insertSql = "INSERT INTO otp_storage (phone_number, otp, expiration_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, phoneNumber);
            pstmt.setString(2, otp);
            pstmt.setTimestamp(3, Timestamp.valueOf(expirationTime));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error storing OTP in database", e);
        }
    }

    // Retrieve the stored OTP and expiration time from the database
    public OtpData getOtpData(String phoneNumber) {
        String selectSql = "SELECT otp, expiration_time FROM otp_storage WHERE phone_number = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setString(1, phoneNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String otp = rs.getString("otp");
                    LocalDateTime expirationTime = rs.getTimestamp("expiration_time").toLocalDateTime();
                    return new OtpData(otp, expirationTime);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching OTP from database", e);
        }
        return null; // If no OTP is found
    }

    // Verify the entered OTP against the stored OTP
    public boolean verifyOtp(String phoneNumber, String enteredOtp) {
        OtpData otpData = getOtpData(phoneNumber);
        if (otpData == null) {
            System.out.println("No OTP found for this phone number.");
            return false;
        }

        // Check if the OTP is expired
        if (otpData.getExpirationTime().isBefore(LocalDateTime.now())) {
            System.out.println("The OTP has expired.");
            return false;
        }

        // Check if the entered OTP matches the stored OTP
        if (otpData.getOtp().equals(enteredOtp)) {
            System.out.println("OTP verified successfully.");
            return true;
        } else {
            System.out.println("Invalid OTP.");
            return false;
        }
    }

    // Delete OTP from the database after verification or expiration
    public void removeOtp(String phoneNumber) {
        String deleteSql = "DELETE FROM otp_storage WHERE phone_number = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, phoneNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting OTP from database", e);
        }
    }

    // Generate a random 6-digit OTP
    private String generateOtp() {
        int otp = new Random().nextInt(999999);
        return String.format("%06d", otp);
    }

    public static class OtpData {
        private final String otp;
        private final LocalDateTime expirationTime;

        public OtpData(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
