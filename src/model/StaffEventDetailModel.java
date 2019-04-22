package model;

public class StaffEventDetailModel {
    private String eventName;
    private String siteName;
    private String startDate;
    private String endDate;
    private int durationDays;
    private int capacity;
    private double eventPrice;
    private String description;

    public StaffEventDetailModel (String eventName, String siteName,
                                  String startDate, String endDate,
                                  int durationDays, int capacity,
                                  double eventPrice, String description) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationDays = durationDays;
        this.capacity = capacity;
        this.eventPrice = eventPrice;
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
