import React, {useCallback, useEffect, useState} from 'react';
import {Text, View} from 'react-native';
import {
  ConfigureOptions,
  Location,
  OnChangeEvent,
  OnErrorEvent,
  RNLocation,
} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {Button} from '../../commons/Button';
import {CardLocation} from '../../commons/CardLocation';
import {openAlert, openSettings, Permission} from '../../utils';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

const OPTIONS: ConfigureOptions = {
  allowsBackgroundLocationUpdates: true,
  distanceFilter: 0,

  android: {
    priority: 'highAccuracy',
    provider: 'auto',
    interval: 2000,
    minWaitTime: 1000,
    maxWaitTime: 2000,
  },
  ios: {
    desiredAccuracy: 'best',
    activityType: 'other',
    headingFilter: 0,
    headingOrientation: 'portrait',
    pausesLocationUpdatesAutomatically: false,
    showsBackgroundLocationIndicator: false,
  },
};

export const BackgroundPage = ({back}: PageProps) => {
  const [location, setLocation] = useState<Location | null>(null);

  const [locationAllow, setLocationAllow] = useState(false);
  const [locationTracking, setLocationTracking] = useState(false);

  const onChange = useCallback<OnChangeEvent>(locations => {
    if (locations.length > 0) {
      const item = locations[0];
      setLocation(item);
      console.log(item);
    }
  }, []);

  const onError = useCallback<OnErrorEvent>(error => {
    console.log(error);
    switch (error.code) {
      case 'ERROR_PROVIDER':
        // Location services are disabled.
        break;
      case 'ERROR_PERMISSION':
        // Location "when-in-use" permission is not granted.
        break;
      case 'ERROR_PERMISSION_ALWAYS':
        // Location "always" permission is not granted.
        break;
      case 'ERROR_UNKNOWN':
      default:
        // Errors that may be returned by location services.
        // Most of the time, they will only work during outages.
        break;
    }
  }, []);

  useEffect(() => {
    RNLocation.configure(OPTIONS);
    Permission.LocationAlways.check()
      .then(status => setLocationAllow(status === 'granted'))
      .catch(() => setLocationAllow(false));
  }, []);

  useEffect(() => {
    if (!locationTracking) return;

    const subscription = RNLocation.subscribe();
    subscription.onChange(onChange).onError(onError);
    return () => {
      subscription && subscription.unsubscribe();
    };
  }, [locationTracking, onChange, onError]);

  const start = () => {
    setLocationTracking(true);
  };

  const stop = () => {
    setLocationTracking(false);
  };

  const request = async () => {
    try {
      const status = await Permission.Location.request();
      if (status !== 'granted') throw new Error('When in use not granted.');

      const statusAlways = await Permission.LocationAlways.request();
      if (statusAlways !== 'granted') throw new Error('Always not granted.');

      setLocationAllow(true);
    } catch (err) {
      openAlert('Location Permission');
      setLocationAllow(false);
    }
  };

  return (
    <Screen>
      <Screen.Header
        left={'Back'}
        leftProps={{onPress: back}}
        right={'Config'}
        rightProps={{onPress: openSettings}}>
        <Screen.Title>Background Location</Screen.Title>
        <Screen.Subtitle>Tracking</Screen.Subtitle>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            To track the user's location when the app is in the background, the
            "ALWAYS" permission is required.
          </Text>

          <Button
            title={'Request Permission'}
            onPress={request}
            style={PageStyle.button}
          />

          {!locationTracking ? (
            <Button
              disabled={!locationAllow}
              title={'Start'}
              onPress={start}
              style={PageStyle.button}
            />
          ) : (
            <Button
              disabled={!locationAllow}
              title={'Stop'}
              onPress={stop}
              style={PageStyle.button}
            />
          )}

          {locationTracking && location && (
            <CardLocation location={location} style={{marginTop: 8}} />
          )}
        </View>
      </Screen.Content>
    </Screen>
  );
};
