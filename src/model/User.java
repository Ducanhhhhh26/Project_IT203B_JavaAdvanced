package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password_hash;
    private Role role;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private Timestamp createdAt;

    public User() {}

    public User(int id, String username, String password_hash, Role role, String fullName, String email, String phone, String department, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
