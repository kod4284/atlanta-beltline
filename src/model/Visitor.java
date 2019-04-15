package model;

import java.util.ArrayList;

public class Visitor extends User {

    private int myEventVisit;
    private int mySiteVisit;

    public Visitor(String username, String firstName, String lastName, String password, String userType, ArrayList<String> email, String status, int emailCount, int myEventVisit, int mySiteVisit) {
        super(username, firstName, lastName, password, userType, email, status, emailCount);
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
