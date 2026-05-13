package com.parking.enums;

public enum SubscriptionStatus {
    ACTIVE("Hoạt động"),
    EXPIRED("Đã hết hạn");

    private final String displayName;

    SubscriptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}