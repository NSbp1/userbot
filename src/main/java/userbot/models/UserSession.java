package userbot.models;

import lombok.Data;

@Data  // This annotation automatically generates getters, setters, toString, equals, and hashCode methods
public class UserSession {
    private String phoneNumber;
    private String name;
    private String surname;
    private String email;
    private String stage;
    private Integer id;

    // Default constructor (no arguments)
  /*  public UserSession() {
    } */

    // Constructor that accepts phone number
    /*public UserSession(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
*/


    // Constructor with all parameters
    public UserSession(Integer id, String phoneNumber, String stage) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.stage = stage;

    }


}
