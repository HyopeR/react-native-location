#import "RNLocationConstants.h"

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

const RNLocationPermissionStatusConst RNLocationPermissionStatus = {
    .GRANTED = @"granted",
    .DENIED = @"denied",
    .BLOCKED = @"blocked"
};
