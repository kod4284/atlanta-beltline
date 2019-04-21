package model;

public class EventDetail {
    private String eventName;
    private String siteName;
    private String startDate;
    private int ticketRemaining;

    public EventDetail (String eventName, String siteName, String startDate,
                        int ticketRemaining) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.startDate = startDate;
        this.ticketRemaining = ticketRemaining;
    }

    public int getTicketRemaining() {
        return ticketRemaining;
    }

    public void setTicketRemaining(int ticketRemaining) {
        this.ticketRemaining = ticketRemaining;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
