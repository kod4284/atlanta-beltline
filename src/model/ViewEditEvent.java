package model;

public class ViewEditEvent {

    public String eventName;
    public String siteName;
    public String startDate;

    public ViewEditEvent(String eventName, String siteName, String startDate) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.startDate = startDate;
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

    @Override
    public String toString() {
        return "ViewEditEvent{" +
                "eventName='" + eventName + '\'' +
                ", siteName='" + siteName + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
