package model;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private Integer supportStaffId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED
    private String prepStatus; // NOT_READY, PREPARING, READY
    private double totalCost;
    private int rating;
    private String feedback;
    private Timestamp createdAt;

    public Booking() {}

    public Booking(int id, int userId, int roomId, Integer supportStaffId, Timestamp startTime, Timestamp endTime, String status, String prepStatus, double totalCost, int rating, String feedback, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.supportStaffId = supportStaffId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.prepStatus = prepStatus;
        this.totalCost = totalCost;
        this.rating = rating;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public Integer getSupportStaffId() { return supportStaffId; }
    public void setSupportStaffId(Integer supportStaffId) { this.supportStaffId = supportStaffId; }
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrepStatus() { return prepStatus; }
    public void setPrepStatus(String prepStatus) { this.prepStatus = prepStatus; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
