package model;

import com.mysql.cj.jdbc.ha.ServerAffinityStrategy;
import javafx.beans.property.SimpleStringProperty;

public class StaffViewScheduleData {
    private SimpleStringProperty eventName;
    private SimpleStringProperty siteName;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private int staffCount;
    private SimpleStringProperty description;

    public StaffViewScheduleData(SimpleStringProperty eventName, SimpleStringProperty siteName,
                                 SimpleStringProperty startDate, SimpleStringProperty endDate,
                                 int staffCount, SimpleStringProperty description) {
        this.eventName = eventName;
        this.siteName = siteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.staffCount= staffCount;
        this.description = description;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
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

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }
}
