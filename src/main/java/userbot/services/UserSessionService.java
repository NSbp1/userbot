package userbot.services;

import org.springframework.stereotype.Service;
import userbot.DatabaseConnector;
import userbot.models.UserSession;

import java.sql.*;

@Service
public class UserSessionService {
    private final String INSERT_SESSION_SQL = "INSERT INTO user_sessions (phone_number, stage, name, surname, email) VALUES (?, ?, ?, ?, ?)";
    private final String SELECT_SESSION_SQL = "SELECT * FROM user_sessions WHERE phone_number = ?";
    private final String UPDATE_SESSION_SQL = "UPDATE user_sessions SET stage = ?, name = ?, surname = ?, email = ? WHERE phone_number = ?";
    private final String DELETE_SESSION_SQL = "DELETE FROM user_sessions WHERE id = ?";

    // Create a new user session
    public void createUserSession(UserSession session) throws SQLException {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SESSION_SQL)) {
            preparedStatement.setString(1, session.getPhoneNumber());
            preparedStatement.setString(2, session.getStage());
            preparedStatement.setString(3, session.getName());
            preparedStatement.setString(4, session.getSurname());
            preparedStatement.setString(5, session.getEmail());
            preparedStatement.executeUpdate();
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
    public void deleteUserSession(Long sessionId) throws SQLException {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SESSION_SQL)) {
            preparedStatement.setLong(1, sessionId);
            preparedStatement.executeUpdate();
        }
    }
}
