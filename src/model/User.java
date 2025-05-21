package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String note;

    public User() {}

    public User(int id, String username, String password, String fullName, String role, String note) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 