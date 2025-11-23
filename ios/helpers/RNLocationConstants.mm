#import "RNLocationConstants.h"

const RNLocationEventConst RNLocationEvent = {
    .ON_CHANGE = @"onChange",
    .ON_ERROR = @"onError"
};

const RNLocationErrorConst RNLocationError = {
    .PROVIDER = @"ERROR_PROVIDER",
    .PERMISSION = @"ERROR_PERMISSION",
    .PERMISSION_ALWAYS = @"ERROR_PERMISSION_ALWAYS",
    .UNKNOWN = @"ERROR_UNKNOWN"
};

const RNLocationPermissionStatusConst RNLocationPermissionStatus = {
    .GRANTED = @"granted",
    .DENIED = @"denied",
    .BLOCKED = @"blocked"
};
