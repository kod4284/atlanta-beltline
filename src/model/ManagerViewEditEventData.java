package model;

import java.util.List;

public class ManagerViewEditEventData {
    private String eventName;
    private double eventPrice;
    private String eventStartDate;
    private String eventEndDate;
    private int minimumStaffRequired;
    private int eventCapacity;
    private List<String> allAvailableStaff;
    private String description;

    public ManagerViewEditEventData(String eventName, double eventPrice, String eventStartDate, String eventEndDate, int minimumStaffRequired, int eventCapacity, List<String> allAvailableStaff, String description) {
        this.eventName = eventName;
        this.eventPrice = eventPrice;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.minimumStaffRequired = minimumStaffRequired;
        this.eventCapacity = eventCapacity;
        this.allAvailableStaff = allAvailableStaff;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public int getMinimumStaffRequired() {
        return minimumStaffRequired;
    }

    public void setMinimumStaffRequired(int minimumStaffRequired) {
        this.minimumStaffRequired = minimumStaffRequired;
    }

    public int getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(int eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public List<String> getAllAvailableStaff() {
        return allAvailableStaff;
    }

    public void setAllAvailableStaff(List<String> allAvailableStaff) {
        this.allAvailableStaff = allAvailableStaff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ManagerViewEditEventData{" +
                "eventName='" + eventName + '\'' +
                ", eventPrice=" + eventPrice +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                ", minimumStaffRequired=" + minimumStaffRequired +
                ", eventCapacity=" + eventCapacity +
                ", allAvailableStaff=" + allAvailableStaff +
                ", description='" + description + '\'' +
                '}';
    }
}
