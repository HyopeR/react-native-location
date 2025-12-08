const {
  withAndroidManifest,
  withInfoPlist,
  createRunOncePlugin,
} = require('@expo/config-plugins');
const {Description, Android, Ios} = require('./plugin.utils');

/**
 * @param {import("@expo/config-plugins").ExportedConfigWithProps} config
 * @param {Record<String, any>} props
 */
function withLocationAndroid(config, props = {}) {
  return withAndroidManifest(config, resource => {
    const manifest = resource.modResults.manifest;

    manifest['uses-permission'] = manifest['uses-permission'] || [];
    Android.addPermission(
      manifest['uses-permission'],
      'android.permission.ACCESS_FINE_LOCATION',
    );
    Android.addPermission(
      manifest['uses-permission'],
      'android.permission.ACCESS_COARSE_LOCATION',
    );

    if (props.background) {
      Android.addPermission(
        manifest['uses-permission'],
        'android.permission.ACCESS_BACKGROUND_LOCATION',
      );
      Android.addPermission(
        manifest['uses-permission'],
        'android.permission.FOREGROUND_SERVICE',
      );
      Android.addPermission(
        manifest['uses-permission'],
        'android.permission.FOREGROUND_SERVICE_LOCATION',
      );
      Android.addPermission(
        manifest['uses-permission'],
        'android.permission.POST_NOTIFICATIONS',
      );

      manifest.application[0].service = manifest.application[0].service || [];
      Android.addService(manifest.application[0].service, {
        'android:name': '.RNLocationForeground',
        'android:exported': 'false',
        'android:foregroundServiceType': 'location',
      });
    }

    return resource;
  });
}

/**
 * @param {import("@expo/config-plugins").ExportedConfigWithProps} config
 * @param {Record<String, any>} props
 */
function withLocationIos(config, props = {}) {
  return withInfoPlist(config, resource => {
    const info = resource.modResults;
    const values = {
      foreground: {
        key: 'NSLocationWhenInUseUsageDescription',
        value: props?.foregroundDescription || Description.foreground,
      },
      background: {
        key: 'NSLocationAlwaysAndWhenInUseUsageDescription',
        value: props?.backgroundDescription || Description.background,
      },
    };

    Ios.addPermission(info, values.foreground.key, values.foreground.value);

    if (props.background) {
      Ios.addPermission(info, values.background.key, values.background.value);
      info.UIBackgroundModes = info.UIBackgroundModes || [];
      Ios.addBackgroundMode(info.UIBackgroundModes, 'location');
    }

    return resource;
  });
}

/**
 * @type {import("@expo/config-plugins").ConfigPlugin<Record<string, any>>}
 */
const withLocation = (config, props) => {
  config = withLocationAndroid(config, props);
  config = withLocationIos(config, props);
  return config;
};

module.exports = createRunOncePlugin(
  withLocation,
  '@hyoper/rn-location',
  '1.0.0',
);
