package model;

import javafx.beans.property.SimpleStringProperty;

public class VisitorExploreSiteData {

    private SimpleStringProperty siteName;
    private int eventCount;
    private int totalVisits;
    private int myVisits;
    public VisitorExploreSiteData(SimpleStringProperty siteName,
                                  int eventCount, int totalVisits, int myVisits) {
        this.siteName = siteName;
        this.eventCount = eventCount;
        this.totalVisits = totalVisits;
        this.myVisits = myVisits;
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

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public int getMyVisits() {
        return myVisits;
    }

    public void setMyVisits(int myVisits) {
        this.myVisits = myVisits;
    }
}
