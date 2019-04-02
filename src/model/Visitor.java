package model;

import java.util.ArrayList;

public class Visitor extends User {

    private int myEventVisit;
    private int mySiteVisit;

    public Visitor(String firstName, String lastName, String password,
                   ArrayList<String> email, String status, int emailCount) {
        super(firstName, lastName, password, email, status, emailCount);
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
