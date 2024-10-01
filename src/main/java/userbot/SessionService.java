/*package userbot.services;

import org.springframework.stereotype.Service;
import userbot.DatabaseConnector;
import userbot.models.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class SessionService {

    // Create a new session for the user
    public UserSession createNewSession(UserSession userSession) throws SQLException {
        String query = "INSERT INTO user_sessions (phone_number, stage, session_data) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userSession.getPhoneNumber());
            statement.setString(2, userSession.getStage());  // Change setLong to setString for stage
            statement.setString(3, userSession.getSessionData());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                Long sessionId = rs.getLong(1);
                userSession.setId(sessionId); // Set the ID to the newly created session
                return userSession; // Return the created session
            }
            return null;
        }
    }

    // Get an existing session by phone number
    public UserSession getSession(String phoneNumber) throws SQLException {
        String query = "SELECT id, stage, session_data FROM user_sessions WHERE phone_number = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String stage = resultSet.getString("stage");
                String sessionData = resultSet.getString("session_data");
                return new UserSession(id, phoneNumber, stage, sessionData);
            } else {
                return null;
            }
        }
    }

    // Update the session with new stage or session data
    public void updateSession(UserSession session) throws SQLException {
        String query = "UPDATE user_sessions SET stage = ?, session_data = ? WHERE id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, session.getStage());  // Change setInt to setString for stage
            statement.setString(2, session.getSessionData());
            statement.setLong(3, session.getId());
            statement.executeUpdate();
        }
    }

    // Clear session after use (optional)
    public void clearSession(String phoneNumber) throws SQLException {
        String query = "DELETE FROM user_sessions WHERE phone_number = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber);
            statement.executeUpdate();
        }
    }
}
*/