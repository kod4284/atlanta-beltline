package model;

import javafx.beans.property.SimpleStringProperty;

public class Email {
    private final SimpleStringProperty email;

    public Email(String email) {
        this.email = new SimpleStringProperty(email);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
