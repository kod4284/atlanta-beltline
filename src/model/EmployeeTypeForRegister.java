package model;

public enum EmployeeTypeForRegister {
    MANAGER("Manager"), STAFF("Staff");

    final String employeeType;

    EmployeeTypeForRegister(String employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public String toString() {
        return employeeType;
    }
}
