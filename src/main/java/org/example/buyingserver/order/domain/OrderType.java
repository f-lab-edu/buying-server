package org.example.buyingserver.order.domain;

public enum OrderType {
    ORDER,        // O : 구매
    SELL_ORDER;

    public String getCode() {
        return switch (this) {
            case ORDER -> "O";
            case SELL_ORDER -> "S";
        };
    }
}