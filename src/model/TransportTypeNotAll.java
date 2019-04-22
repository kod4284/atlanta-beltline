package model;

public enum TransportTypeNotAll {
    MARTA("MARTA"), BUS("Bus"), BIKE("Bike"), OTHER("Other");

    final String transportType;

    TransportTypeNotAll(String transportType) {
        this.transportType = transportType;
    }

    @Override
    public String toString() {
        return transportType;
    }
}
