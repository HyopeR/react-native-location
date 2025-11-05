import React, {useState} from 'react';
import {Text, View} from 'react-native';
import {ConfigureOptions, Location} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {openSettings} from '../../utils';
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
  const [locationConfigured, setLocationConfigured] = useState(false);
  const [locationUsage, setLocationUsage] = useState(false);

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
        </View>
      </Screen.Content>
    </Screen>
  );
};
