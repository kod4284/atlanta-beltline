package model;


import javafx.beans.property.SimpleStringProperty;

public class VisitorExploreEventData {
    private final SimpleStringProperty eventName;
    private final SimpleStringProperty siteName;
    private final double ticketPrice;
    private final int ticketRemaining;
    private final int totalVisits;
    private final int myVisits;

    public VisitorExploreEventData(SimpleStringProperty eventName,
                                   SimpleStringProperty siteName,
                                   double ticketPrice,
                                   int ticketRemaining, int totalVisits,
                                   int myVisits) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.ticketPrice = ticketPrice;
        this.ticketRemaining = ticketRemaining;
        this.totalVisits = totalVisits;
        this.myVisits = myVisits;
    }
}
