package com.app.TaxiReservation.util;

public enum Status {
    AV("Available"),
    BUSY("Busy");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
