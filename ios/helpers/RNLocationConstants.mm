#import "RNLocationConstants.h"

const RNLocationPermissionStatusConst RNLocationPermissionStatus = {
    .GRANTED = @"granted",
    .DENIED = @"denied",
    .BLOCKED = @"blocked"
};

const RNLocationEventConst RNLocationEvent = {
    .ON_CHANGE = @"onChange",
    .ON_ERROR = @"onError"
};

const RNLocationErrorConst RNLocationError = {
    .SETUP = @"ERROR_SETUP",
    .PROVIDER = @"ERROR_PROVIDER",
    .PERMISSION = @"ERROR_PERMISSION",
    .PERMISSION_ALWAYS = @"ERROR_PERMISSION_ALWAYS",
    .PERMISSION_NOTIFICATION = @"ERROR_PERMISSION_NOTIFICATION",
    .UNKNOWN = @"ERROR_UNKNOWN"
};

const RNLocationErrorMessageConst RNLocationErrorMessage = {
    .UNKNOWN = @"Unknown error.",
    .SETUP = @"Setup missing: ",
    .SETUP_RUNTIME = @"Setup runtime error: ",
    .PROVIDER = @"No location manager is available.",
    .PERMISSION = @"Location when-in-use permission is not granted.",
    .PERMISSION_ALWAYS = @"Location always permission is not granted.",
    .PERMISSION_NOTIFICATION = @"Notification permission is not granted.",
    .LOCATION_TIMEOUT = @"Location fetching operation timed out.",
    .LOCATION_PROVIDER_TEMPORARY = @"Location provider is temporarily unavailable.",
    .PLATFORM_SUPPORT = @"This feature is not supported on ios.",
};
