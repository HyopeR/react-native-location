package com.hyoper.location.helpers;

public class RNLocationConstants {
    public static class Event {
        public static final String ON_CHANGE = "onChange";
        public static final String ON_ERROR = "onError";
    }

    public static class Error {
        public static final String SETUP = "ERROR_SETUP";
        public static final String PROVIDER = "ERROR_PROVIDER";
        public static final String PERMISSION = "ERROR_PERMISSION";
        public static final String PERMISSION_ALWAYS = "ERROR_PERMISSION_ALWAYS";
        public static final String PERMISSION_NOTIFICATION = "ERROR_PERMISSION_NOTIFICATION";
        public static final String UNKNOWN = "ERROR_UNKNOWN";
    }

    public static class PermissionStatus {
        public static final String GRANTED = "granted";
        public static final String DENIED = "denied";
        public static final String BLOCKED = "blocked";
    }
}
