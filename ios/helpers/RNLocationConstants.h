#import <Foundation/Foundation.h>

typedef struct {
    __unsafe_unretained NSString * _Nonnull GRANTED;
    __unsafe_unretained NSString * _Nonnull DENIED;
    __unsafe_unretained NSString * _Nonnull BLOCKED;
} RNLocationPermissionStatusConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull ON_CHANGE;
    __unsafe_unretained NSString * _Nonnull ON_ERROR;
} RNLocationEventConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull UNKNOWN;
    __unsafe_unretained NSString * _Nonnull SETUP;
    __unsafe_unretained NSString * _Nonnull PROVIDER;
    __unsafe_unretained NSString * _Nonnull PERMISSION;
    __unsafe_unretained NSString * _Nonnull PERMISSION_ALWAYS;
    __unsafe_unretained NSString * _Nonnull PERMISSION_NOTIFICATION;
} RNLocationErrorConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull UNKNOWN;
    __unsafe_unretained NSString * _Nonnull SETUP;
    __unsafe_unretained NSString * _Nonnull SETUP_RUNTIME;
    __unsafe_unretained NSString * _Nonnull PROVIDER;
    __unsafe_unretained NSString * _Nonnull PERMISSION;
    __unsafe_unretained NSString * _Nonnull PERMISSION_ALWAYS;
    __unsafe_unretained NSString * _Nonnull PERMISSION_NOTIFICATION;
    __unsafe_unretained NSString * _Nonnull LOCATION_TIMEOUT;
    __unsafe_unretained NSString * _Nonnull LOCATION_PROVIDER_TEMPORARY;
    __unsafe_unretained NSString * _Nonnull PLATFORM_SUPPORT;
} RNLocationErrorMessageConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull ICON;
    __unsafe_unretained NSString * _Nonnull TITLE;
    __unsafe_unretained NSString * _Nonnull CONTENT;
} RNLocationNotifyConst;

extern const RNLocationPermissionStatusConst RNLocationPermissionStatus;
extern const RNLocationEventConst RNLocationEvent;
extern const RNLocationErrorConst RNLocationError;
extern const RNLocationErrorMessageConst RNLocationErrorMessage;
extern const RNLocationNotifyConst RNLocationNotify;
