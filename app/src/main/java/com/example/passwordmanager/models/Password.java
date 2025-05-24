package com.example.passwordmanager.models;

public class Password {
    private int id;
    private String masterUsername;
    private String serviceName;
    private String username;
    private String password;
    private String notes;

    public Password(int id, String masterUsername, String serviceName, String username, String password, String notes) {
        this.id = id;
        this.masterUsername = masterUsername;
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMasterUsername() { return masterUsername; }
    public void setMasterUsername(String masterUsername) { this.masterUsername = masterUsername; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}