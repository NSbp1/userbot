package userbot;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DatabaseConnector {
        private static final String URL = "jdbc:postgresql://localhost:5432/ChatBot_db";
        private static final String USER = "postgres";
        private static final String PASSWORD = "121314plSql";

        public static Connection connect() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

