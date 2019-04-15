package model;

import java.util.ArrayList;

public class Staff extends Employee {
    private int numEventShifts;

    public Staff(String username, String firstName, String lastName, String password, String userType, ArrayList<String> email, String status, int emailCount, String address, String city, String state, int zipcode, int employee, String phone, int numEventShifts) {
        super(username, firstName, lastName, password, userType, email, status, emailCount, address, city, state, zipcode, employee, phone);
        this.numEventShifts = numEventShifts;
    }

    public int getNumEventShifts() {
        return numEventShifts;
    }

    public void setNumEventShifts(int numEventShifts) {
        this.numEventShifts = numEventShifts;
    }
}
