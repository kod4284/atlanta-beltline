package model;

import javafx.beans.property.SimpleStringProperty;

public class ManagerViewEditEventTableData {
    private SimpleStringProperty date;
    private SimpleStringProperty dailyVisits;
    private SimpleStringProperty dailyRevenue;

    public ManagerViewEditEventTableData(SimpleStringProperty date, SimpleStringProperty dailyVisits, SimpleStringProperty dailyRevenue) {
        this.date = date;
        this.dailyVisits = dailyVisits;
        this.dailyRevenue = dailyRevenue;
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

    public String getDailyVisits() {
        return dailyVisits.get();
    }

    public SimpleStringProperty dailyVisitsProperty() {
        return dailyVisits;
    }

    public void setDailyVisits(String dailyVisits) {
        this.dailyVisits.set(dailyVisits);
    }

    public String getDailyRevenue() {
        return dailyRevenue.get();
    }

    public SimpleStringProperty dailyRevenueProperty() {
        return dailyRevenue;
    }

    public void setDailyRevenue(String dailyRevenue) {
        this.dailyRevenue.set(dailyRevenue);
    }
}
