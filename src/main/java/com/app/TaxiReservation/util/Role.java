package com.app.TaxiReservation.util;

public enum Role {
    ADMIN("Admin"),
    USER("User"),
    DRIVER("Driver"),
    GUESTUSER("Guest User");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
