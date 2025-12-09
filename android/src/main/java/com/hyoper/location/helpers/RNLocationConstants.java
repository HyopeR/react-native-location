package com.hyoper.location.helpers;

public class RNLocationConstants {
    public static class PermissionStatus {
        public static final String GRANTED = "granted";
        public static final String DENIED = "denied";
        public static final String BLOCKED = "blocked";
    }

    public static class Event {
        public static final String ON_CHANGE = "onChange";
        public static final String ON_ERROR = "onError";
    }

    public static class Error {
        public static final String UNKNOWN = "ERROR_UNKNOWN";
        public static final String SETUP = "ERROR_SETUP";
        public static final String PROVIDER = "ERROR_PROVIDER";
        public static final String PERMISSION = "ERROR_PERMISSION";
        public static final String PERMISSION_ALWAYS = "ERROR_PERMISSION_ALWAYS";
        public static final String PERMISSION_NOTIFICATION = "ERROR_PERMISSION_NOTIFICATION";
    }

    public static class ErrorMessage {
        public static final String UNKNOWN = "Unknown error.";
        public static final String SETUP = "Setup missing: ";
        public static final String SETUP_RUNTIME = "Setup runtime error: ";
        public static final String PROVIDER = "No location manager is available.";
        public static final String PERMISSION = "Location when-in-use permission is not granted.";
        public static final String PERMISSION_ALWAYS = "Location always permission is not granted.";
        public static final String PERMISSION_NOTIFICATION = "Notification permission is not granted.";
        public static final String LOCATION_TIMEOUT = "Location fetching operation timed out.";
        public static final String LOCATION_PROVIDER_TEMPORARY = "Location provider is temporarily unavailable.";
        public static final String ACTIVITY = "Activity is not available.";
    }

    public static class Notify {
        public static final String ICON = "ic_launcher";
        public static final String TITLE = "Location Service Running";
        public static final String CONTENT = "Location is being used by the app.";
    }
}
