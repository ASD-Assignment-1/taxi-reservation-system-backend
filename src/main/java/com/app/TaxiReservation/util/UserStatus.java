package com.app.TaxiReservation.util;

public enum UserStatus {
    USER("Register user"),
    GUEST("Guest User");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
