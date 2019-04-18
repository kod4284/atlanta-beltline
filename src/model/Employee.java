package model;

import java.util.ArrayList;

public class Employee extends User {
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private int employee;
    private String phone;

    public Employee(String username, String firstName, String lastName,
                    String password, String userType, ArrayList<String>
                            email, String status, int emailCount, String
                            address, String city, String state, String zipcode,
                    int employee, String phone) {
        super(username, firstName, lastName, password, userType, email, status, emailCount);
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.employee = employee;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
