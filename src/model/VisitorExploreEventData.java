package model;


import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;

public class VisitorExploreEventData {
    private final SimpleStringProperty eventName;
    private final SimpleStringProperty siteName;
    private final double ticketPrice;
    private final int ticketRemaining;
    private final int totalVisits;
    private final int myVisits;
    private final SimpleStringProperty startDate;

    public VisitorExploreEventData(SimpleStringProperty eventName,
                                   SimpleStringProperty siteName,
                                   double ticketPrice,
                                   int ticketRemaining, int totalVisits,
                                   int myVisits,
                                   SimpleStringProperty startDate) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.ticketPrice = ticketPrice;
        this.ticketRemaining = ticketRemaining;
        this.totalVisits = totalVisits;
        this.myVisits = myVisits;
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
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

    public String getSiteName() {
        return siteName.get();
    }

    public SimpleStringProperty siteNameProperty() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName.set(siteName);
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public int getTicketRemaining() {
        return ticketRemaining;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public int getMyVisits() {
        return myVisits;
    }
}
