package model;

import javafx.beans.property.SimpleStringProperty;

public class AdminManageUserData {
    private final SimpleStringProperty username;
    private int emailCount;
    private final SimpleStringProperty userType;
    private final SimpleStringProperty status;

    public AdminManageUserData(SimpleStringProperty username, int emailCount, SimpleStringProperty userType, SimpleStringProperty status) {
        this.username = username;
        this.emailCount = emailCount;
        this.userType = userType;
        this.status = status;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public int getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public String getUserType() {
        return userType.get();
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
