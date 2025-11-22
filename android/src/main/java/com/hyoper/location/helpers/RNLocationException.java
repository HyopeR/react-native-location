package com.hyoper.location.helpers;

public class RNLocationException extends Exception {
    public final String code;
    public final boolean critical;

    public RNLocationException(String code, String message) {
        super(message);
        this.code = code;
        this.critical = false;
    }

    public RNLocationException(String code, String message, boolean critical) {
        super(message);
        this.code = code;
        this.critical = critical;
    }
}
