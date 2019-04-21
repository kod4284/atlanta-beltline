package model;

public enum UserTypeForFilter {
    ALL("-- ALL --"), MANAGER("Manager"), STAFF("Staff"), USER("User"),
    VISITOR("Visitor");

    final String userType;

    UserTypeForFilter(String type) {
        userType = type;
    }

    @Override
    public String toString() {
        return userType;
    }
}
