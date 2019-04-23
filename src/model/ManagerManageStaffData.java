package model;

import controller.ManagerManageEvent;
import javafx.beans.property.SimpleStringProperty;

public class ManagerManageStaffData {
    private SimpleStringProperty staffName;
    private int numEventShifts;

    public ManagerManageStaffData (SimpleStringProperty staffName, int numEventShifts) {
        this.staffName = staffName;
        this.numEventShifts = numEventShifts;
    }

    public String getStaffName() {
        return staffName.get();
    }

    public SimpleStringProperty staffNameProperty() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName.set(staffName);
    }

    public int getNumEventShifts() {
        return numEventShifts;
    }

    public void setNumEventShifts(int numEventShifts) {
        this.numEventShifts = numEventShifts;
    }
}
