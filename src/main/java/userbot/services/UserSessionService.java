package userbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import userbot.DatabaseConnector;
import userbot.models.UserSession;

import java.sql.*;

@Service
public class UserSessionService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationStageHandler.class);

    private final String INSERT_SESSION_SQL = "INSERT INTO user_sessions (phone_number, stage, name, surname, email) VALUES (?, ?, ?, ?, ?)";
    private final String SELECT_SESSION_SQL = "SELECT * FROM user_sessions WHERE phone_number = ?";
    private final String UPDATE_SESSION_SQL = "UPDATE user_sessions SET stage = ?, name = ?, surname = ?, email = ? WHERE phone_number = ?";
    private final String DELETE_SESSION_SQL = "DELETE FROM user_sessions WHERE id = ?";

    // Create a new user session
    // Create a new user session with ID retrieval
    public void createUserSession(UserSession session) throws SQLException {
        String[] generatedColumns = {"id"}; // Specify to return the generated id
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SESSION_SQL, generatedColumns)) {
            preparedStatement.setString(1, session.getPhoneNumber());
            preparedStatement.setString(2, session.getStage());
            preparedStatement.setString(3, session.getName());
            preparedStatement.setString(4, session.getSurname());
            preparedStatement.setString(5, session.getEmail());
            preparedStatement.executeUpdate();

            // Retrieve generated session ID and set it to the session object
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1)); // Set the session ID from generated key
                }
            }
        }
    }



    // Retrieve a user session by phone number
    public UserSession getUserSessionByPhoneNumber(String phoneNumber) throws SQLException {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SESSION_SQL)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserSession session = new UserSession();
                session.setId(resultSet.getInt("id"));
                session.setPhoneNumber(resultSet.getString("phone_number"));
                session.setStage(resultSet.getString("stage"));
                session.setName(resultSet.getString("name"));
                session.setSurname(resultSet.getString("surname"));
                session.setEmail(resultSet.getString("email"));
                return session;
            }
        }
        return null; // No session found
    }

    // Update an existing user session
    public void updateUserSession(UserSession session) throws SQLException {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SESSION_SQL)) {
            preparedStatement.setString(1, session.getStage());
            preparedStatement.setString(2, session.getName());
            preparedStatement.setString(3, session.getSurname());
            preparedStatement.setString(4, session.getEmail());
            preparedStatement.setString(5, session.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
    }

    // Delete the user session
    public void deleteUserSession(Integer sessionId) throws SQLException {
        String deleteSQL = "DELETE FROM user_sessions WHERE id = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, sessionId);
            preparedStatement.executeUpdate();
        }
    }


}
