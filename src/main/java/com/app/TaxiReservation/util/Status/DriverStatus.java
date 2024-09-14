package com.app.TaxiReservation.util.Status;

public enum DriverStatus {
    AV("Available"),
    BUSY("Busy");

    private final String displayName;

    DriverStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
