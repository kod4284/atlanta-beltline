package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String userType;
    private ArrayList<String> email;
    private String status;
    private String EmployeeType;
    private int emailCount;
    private String siteName;

    public User(String username, String firstName, String lastName, String
            password, String userType, ArrayList<String> email, String status,
                int emailCount) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.status = status;
        this.emailCount = emailCount;
    }
    public User(String password, String status, String
            userType) {
        this.password = password;
        this.status = status;
        this.userType = userType;
    }
    public User(String username, String password, String status, String
            userType) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.userType = userType;
    }

    public String getEmployeeType() {
        return EmployeeType;
    }

    public void setEmployeeType(String employeeType) {
        EmployeeType = employeeType;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void addEmail(String email) {
        this.email.add(email);
    }
    public boolean removeEmail(String email) {
        return this.email.remove(email);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public boolean isEmployee() {
        return this.userType.equals(UserType.EMPLOYEE.toString());
    }
    public boolean isEmployeeVisitor() {
        return this.userType.equals(UserType.EMPLOYEE_VISITOR.toString());
    }
    public boolean isUser() {
        return this.userType.equals(UserType.USER.toString());
    }
    public boolean isVisitor() {
        return this.userType.equals(UserType.VISITOR.toString());
    }
    public boolean isManager() {
        return this.EmployeeType.equals(model.EmployeeType.MANAGER.toString());
    }
    public boolean isAdmin() {
        return this.EmployeeType.equals(model.EmployeeType.ADMIN.toString());
    }
    public boolean isStaff() {
        return this.EmployeeType.equals(model.EmployeeType.STAFF.toString());
    }
    @Override
    public String toString() {
        String str = "username: " + username;
        return str;
    }
}
