import React, {useEffect, useState} from 'react';
import {Text, View} from 'react-native';
import {ConfigureOptions, Location, RNLocation} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {Button} from '../../commons/Button';
import {openAlert, openSettings, Permission} from '../../utils';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

const Options: ConfigureOptions = {
  // Shared
  allowsBackgroundLocationUpdates: true,
  distanceFilter: 0,
  desiredAccuracy: {
    ios: 'best',
    android: 'highAccuracy',
  },

  // Android only
  androidProvider: 'auto',
  interval: 2000,
  fastestInterval: 1000,
  maxWaitTime: 2000,

  // Ios only
  activityType: 'other',
  headingFilter: 0,
  headingOrientation: 'portrait',
  pausesLocationUpdatesAutomatically: false,
  showsBackgroundLocationIndicator: false,
};

export const BackgroundPage = ({back}: PageProps) => {
  const [location, setLocation] = useState<Location | null>(null);
  const [locationStarted, setLocationStarted] = useState(false);

  const [locationAllow, setLocationAllow] = useState(false);
  const [locationConfigured, setLocationConfigured] = useState(false);

  useEffect(() => {
    Permission.LocationAlways.check()
      .then(status => setLocationAllow(status === 'granted'))
      .catch(() => setLocationAllow(false));
  }, []);

  useEffect(() => {
    if (locationAllow && !locationConfigured) {
      RNLocation.configure(Options);
      setLocationConfigured(true);
    }
  }, [locationAllow, locationConfigured]);

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

  const start = async () => {
    RNLocation.start();
    setLocationStarted(true);
  };

  const stop = async () => {
    RNLocation.stop();
    setLocationStarted(false);
  };

  return (
    <Screen>
      <Screen.Header
        left={'Back'}
        leftProps={{onPress: back}}
        right={'Config'}
        rightProps={{onPress: openSettings}}>
        <Screen.Title>Background Location</Screen.Title>
        <Screen.Subtitle>Usage</Screen.Subtitle>
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
            style={{marginBottom: 8}}
          />

          {!locationStarted ? (
            <Button disabled={!locationAllow} title={'Start'} onPress={start} />
          ) : (
            <Button disabled={!locationAllow} title={'Stop'} onPress={stop} />
          )}
        </View>
      </Screen.Content>
    </Screen>
  );
};
