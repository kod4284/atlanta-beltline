package model;

import javafx.beans.property.SimpleStringProperty;

public class ManagerSiteReportData {
    private final SimpleStringProperty date;
    private final int eventCount;
    private final int staffCount;
    private final int totalVisits;
    private final double totalRevenue;

    public ManagerSiteReportData(SimpleStringProperty date, int eventCount, int staffCount, int totalVisits, double totalRevenue) {
        this.date = date;
        this.eventCount = eventCount;
        this.staffCount = staffCount;
        this.totalVisits = totalVisits;
        this.totalRevenue = totalRevenue;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public int getEventCount() {
        return eventCount;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
