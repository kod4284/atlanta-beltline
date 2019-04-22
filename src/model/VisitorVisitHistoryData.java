package model;

import javafx.beans.property.SimpleStringProperty;

public class VisitorVisitHistoryData {

    private SimpleStringProperty date;
    private SimpleStringProperty event;
    private SimpleStringProperty site;
    private int price;
    public VisitorVisitHistoryData (SimpleStringProperty date,
                                    SimpleStringProperty event,
                                    SimpleStringProperty site,
                                    int price) {
        this.date = date;
        this.event = event;
        this.site = site;
        this.price = price;
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

    public String getEvent() {
        return event.get();
    }

    public SimpleStringProperty eventProperty() {
        return event;
    }

    public void setEvent(String event) {
        this.event.set(event);
    }

    public String getSite() {
        return site.get();
    }

    public SimpleStringProperty siteProperty() {
        return site;
    }

    public void setSite(String site) {
        this.site.set(site);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
