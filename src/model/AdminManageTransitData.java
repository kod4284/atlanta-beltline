package model;

import javafx.beans.property.SimpleStringProperty;

public class AdminManageTransitData {
    private final SimpleStringProperty route;
    private final SimpleStringProperty transportType;
    private final double price;
    private final int connectedSites;
    private final int transitLogged;

    public AdminManageTransitData(SimpleStringProperty route, SimpleStringProperty transportType, double price, int connectedSites, int transitLogged) {
        this.route = route;
        this.transportType = transportType;
        this.price = price;
        this.connectedSites = connectedSites;
        this.transitLogged = transitLogged;
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

    public int getTransitLogged() {
        return transitLogged;
    }
}
