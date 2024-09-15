package com.app.TaxiReservation.util.Status;

public enum UserStatus {
    USER("Register user"),
    GUEST("Guest User"),
    ADMIN("Admin User"),
    DRIVER("Driver User");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
