package model;

import javafx.beans.property.SimpleStringProperty;

public class UserTakeTransitData {
    private final SimpleStringProperty route;
    private final SimpleStringProperty transportType;
    private final double price;
    private final int connectedSites;

    public UserTakeTransitData(SimpleStringProperty route, SimpleStringProperty transportType, double price, int connectedSites) {
        this.route = route;
        this.transportType = transportType;
        this.price = price;
        this.connectedSites = connectedSites;
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

    public int getConnectedSites() {
        return connectedSites;
    }
}
