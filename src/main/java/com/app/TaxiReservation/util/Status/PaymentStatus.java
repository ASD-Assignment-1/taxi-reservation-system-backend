package com.app.TaxiReservation.util.Status;

public enum PaymentStatus {
    PENDING("Pending"),
    DONE("Done");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
