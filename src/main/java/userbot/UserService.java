package userbot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    //checking if a user is already registered
    public User getUserByPhoneNumber(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM customer WHERE phone_number = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("name"), rs.getString("email"), phoneNumber);
            }
        }
        return null;
    }

    //creating a user
    public void createUser(User user) throws SQLException {
        String query = "INSERT INTO customer (name, email, phone_number, created_at) VALUES (?, ?, ?, now())";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.executeUpdate();
        }
    }

   // public void requestLog()
}
