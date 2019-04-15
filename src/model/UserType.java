package model;

public enum UserType {
    EMPLOYEE("Employee"), EMPLOYEE_VISITOR("Employee, Visitor"), USER("User"),
    VISITOR("Visitor");

    final String userType;

    UserType(String type) {
        userType = type;
    }

    @Override
    public String toString() {
        return userType;
    }
}
