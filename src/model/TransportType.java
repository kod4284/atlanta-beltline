package model;

public enum TransportType {
    ALL("-- ALL --"), MARTA("MARTA"), BUS("Bus"), BIKE("Bike"), OTHER("Other");

    final String transportType;

    TransportType(String transportType) {
        this.transportType = transportType;
    }

    @Override
    public String toString() {
        return transportType;
    }
}
