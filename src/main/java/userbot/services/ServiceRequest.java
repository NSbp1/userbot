package userbot.services;

import java.util.UUID;
import lombok.Data;

@Data
public class ServiceRequest {
    private UUID id;
    private UUID userId;
    private String requestType;
    private String description;
    private String status;
    private String createdAt;

    public ServiceRequest(UUID userId, String requestType, String description) {
        this.userId = userId;
        this.requestType = requestType;
        this.description = description;
        this.status = "Pending";  // Default status is Pending
    }

}
