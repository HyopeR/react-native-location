package com.hyoper.location;

public class RNLocationException extends Exception {
    public final String type;
    public final boolean critical;

    public RNLocationException(String type, String message) {
        super(message);
        this.type = type;
        this.critical = false;
    }

    public RNLocationException(String type, String message, boolean critical) {
        super(message);
        this.type = type;
        this.critical = critical;
    }
}
