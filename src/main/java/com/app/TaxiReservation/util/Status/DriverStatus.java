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

    public static DriverStatus fromDisplayName(String displayName) {
        for (DriverStatus status : values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching DriverStatus for display name: " + displayName);
    }
}
