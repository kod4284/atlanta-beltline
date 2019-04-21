package model;

import javafx.beans.property.SimpleStringProperty;

public class UserTransitHistoryData {
    private final SimpleStringProperty date;
    private final SimpleStringProperty route;
    private final SimpleStringProperty transportType;
    private final double price;

    public UserTransitHistoryData(SimpleStringProperty date, SimpleStringProperty route, SimpleStringProperty transportType, double price) {
        this.date = date;
        this.route = route;
        this.transportType = transportType;
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

    public String getRoute() {
        return route.get();
    }

    public SimpleStringProperty routeProperty() {
        return route;
    }

    public void setRoute(String route) {
        this.route.set(route);
    }

    public String getTransportType() {
        return transportType.get();
    }

    public SimpleStringProperty transportTypeProperty() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType.set(transportType);
    }

    public double getPrice() {
        return price;
    }
}
