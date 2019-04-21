package model;

import javafx.beans.property.SimpleStringProperty;

public class ManagerForNum19 {
    private final String name;
    private final String username;

    public ManagerForNum19(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return name;
    }
}
