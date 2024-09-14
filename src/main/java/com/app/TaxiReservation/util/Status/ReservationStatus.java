package com.app.TaxiReservation.util.Status;

public enum ReservationStatus {
    START("Start"),
    END("End");

    private final String displayName;

    ReservationStatus(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
