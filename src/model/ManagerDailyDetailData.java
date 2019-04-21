package model;

import javafx.beans.property.SimpleStringProperty;


public class ManagerDailyDetailData {
    private final SimpleStringProperty eventName;
    private final String staffName;
    private final int visits;
    private final double revenue;

    public ManagerDailyDetailData(SimpleStringProperty eventName, String staffName, int visits, double revenue) {
        this.eventName = eventName;
        this.staffName = staffName;
        this.visits = visits;
        this.revenue = revenue;
    }

    public String getEventName() {
        return eventName.get();
    }

    public SimpleStringProperty eventNameProperty() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public String getStaffName() {
        return staffName;
    }

    public int getVisits() {
        return visits;
    }

    public double getRevenue() {
        return revenue;
    }
}
