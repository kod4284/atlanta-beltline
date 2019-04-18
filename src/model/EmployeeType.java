package model;


public enum EmployeeType {
    ADMIN("Admin"), MANAGER("Manager"), STAFF("Staff");

    final String employeeType;

    EmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public String toString() {
        return employeeType;
    }
}