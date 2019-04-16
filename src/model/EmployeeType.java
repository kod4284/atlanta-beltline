package model;


public enum EmployeeType {
    ADMIN("Admin"), MANAGER("Manager"), STAFF("Staff");

    final String employeeType;

    EmployeeType(String type) {
        employeeType = type;
    }

    @Override
    public String toString() {
        return employeeType;
    }
}