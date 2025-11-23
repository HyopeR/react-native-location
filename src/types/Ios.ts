/**
 * The accuracy of a geographical coordinate for iOS.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationaccuracy?language=objc)
 */
export type IosAccuracy =
  | 'bestForNavigation'
  | 'best'
  | 'nearestTenMeters'
  | 'hundredMeters'
  | 'threeKilometers';

/**
 * Constants indicating the type of activity associated with location updates.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/clactivitytype?language=objc)
 */
export type IosActivityType =
  | 'other'
  | 'automotiveNavigation'
  | 'fitness'
  | 'otherNavigation'
  | 'airborne'; // iOS 12+

/**
 * Constants indicating the physical orientation of the device.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cldeviceorientation?language=objc)
 */
export type IosHeadingOrientation =
  | 'portrait'
  | 'portraitUpsideDown'
  | 'landscapeLeft'
  | 'landscapeRight';
