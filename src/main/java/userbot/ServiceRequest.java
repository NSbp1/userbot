package userbot;

import java.util.UUID;

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

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
