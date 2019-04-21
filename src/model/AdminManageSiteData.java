package model;

import javafx.beans.property.SimpleStringProperty;

public class AdminManageSiteData {
    private final SimpleStringProperty name;
    private final SimpleStringProperty manager;
    private final SimpleStringProperty openEveryday;
    private final SimpleStringProperty username;

    public AdminManageSiteData(SimpleStringProperty name,
                               SimpleStringProperty manager,
                               SimpleStringProperty openEveryday,
                               SimpleStringProperty username) {
        this.name = name;
        this.manager = manager;
        this.openEveryday = openEveryday;
        this.username = username;
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getManager() {
        return manager.get();
    }

    public SimpleStringProperty managerProperty() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager.set(manager);
    }

    public String getOpenEveryday() {
        return openEveryday.get();
    }

    public SimpleStringProperty openEverydayProperty() {
        return openEveryday;
    }

    public void setOpenEveryday(String openEveryday) {
        this.openEveryday.set(openEveryday);
    }
}
