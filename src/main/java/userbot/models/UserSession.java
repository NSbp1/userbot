package userbot.models;

import lombok.Data;

@Data  // This annotation automatically generates getters, setters, toString, equals, and hashCode methods
public class UserSession {
    private String phoneNumber;
    private String name;
    private String surname;
    private String email;
    private String stage;
    private String sessionData;
    private Long id;

    // Default constructor (no arguments)
    public UserSession() {
    }

    // Constructor that accepts phone number
    public UserSession(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Constructor with all parameters
    public UserSession(Long id, String phoneNumber, String stage, String sessionData) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.stage = stage;
        this.sessionData = sessionData;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
