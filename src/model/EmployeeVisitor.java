package model;

import java.util.ArrayList;

public class EmployeeVisitor extends Employee {

    private int myEventVisit;
    private int mySiteVisit;

    public EmployeeVisitor(String username, String firstName, String lastName, String password, String userType, ArrayList<String> email, String status, int emailCount, String address, String city, String state, int zipcode, int employee, String phone, int myEventVisit, int mySiteVisit) {
        super(username, firstName, lastName, password, userType, email, status, emailCount, address, city, state, zipcode, employee, phone);
        this.myEventVisit = myEventVisit;
        this.mySiteVisit = mySiteVisit;
    }

    public int getMyEventVisit() {
        return myEventVisit;
    }

    public void setMyEventVisit(int myEventVisit) {
        this.myEventVisit = myEventVisit;
    }

    public int getMySiteVisit() {
        return mySiteVisit;
    }

    public void setMySiteVisit(int mySiteVisit) {
        this.mySiteVisit = mySiteVisit;
    }
}
