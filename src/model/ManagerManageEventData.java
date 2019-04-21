package model;

import javafx.beans.property.SimpleStringProperty;

public class ManagerManageEventData {
    private SimpleStringProperty name;
    private int staffCount;
    private int duration;
    private int totalVisits;
    private double totalRevenue;

    public ManagerManageEventData(SimpleStringProperty name, int staffCount, int duration, int totalVisits, double totalRevenue) {
        this.name = name;
        this.staffCount = staffCount;
        this.duration = duration;
        this.totalVisits = totalVisits;
        this.totalRevenue = totalRevenue;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
