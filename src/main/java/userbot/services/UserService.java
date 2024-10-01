package userbot.services;

import org.springframework.stereotype.Service;
import userbot.DatabaseConnector;
import userbot.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service  // Add this annotation
public class UserService {

    // Checking if a user is already registered
    public User getUserByPhoneNumber(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM customer WHERE phone_number = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("name"), rs.getString("email"), phoneNumber);
            }
        } catch (SQLException e) {
            // Log and handle SQLException
            throw new SQLException("Error fetching user by phone number", e);
        }
        return null;
    }

    // Creating a user
    public void createUser(User user) throws SQLException {
        String query = "INSERT INTO customer (name, phone_number ,email, created_at) VALUES (?, ?, ?, now())";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle SQLException
            throw new SQLException("Error creating user", e);
        }
    }
}
