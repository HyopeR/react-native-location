import React, {useEffect, useState} from 'react';
import {Text, View} from 'react-native';
import {
  CURRENT_OPTIONS,
  CurrentOptions,
  Location,
  RNLocation,
} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {Button} from '../../commons/Button';
import {CardLocation} from '../../commons/CardLocation';
import {openAlert, openSettings} from '../../utils';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

const OPTIONS: CurrentOptions = {
  ...CURRENT_OPTIONS,
  accuracy: 'high',
  timeout: 10000,
  background: false,
};

export const CurrentPage = ({back}: PageProps) => {
  const [location, setLocation] = useState<Location | null>(null);

  const [locationAllow, setLocationAllow] = useState(false);
  const [locationLoading, setLocationLoading] = useState(false);

  useEffect(() => {
    RNLocation.permission
      .checkLocation()
      .then(status => {
        console.log('check status', status);
        setLocationAllow(status === 'granted');
      })
      .catch(error => {
        console.log('check', error);
        setLocationAllow(false);
      });
  }, []);

  const getCurrentLocation = async () => {
    setLocationLoading(true);

    try {
      const item = await RNLocation.getCurrent(OPTIONS);
      setLocation(item);
      console.log(item);
    } catch (err: any) {
      console.log(err);
      console.log(err?.code);
      console.log(err?.message);
    } finally {
      setLocationLoading(false);
    }
  };

  const request = async () => {
    try {
      const status = await RNLocation.permission.requestLocation();
      console.log('request status', status);

      if (status === 'blocked') openAlert('Location Permission');
      if (status !== 'granted') {
        setLocationAllow(false);
        return;
      }

      setLocationAllow(true);
    } catch (err) {
      console.log('request', err);
    }
  };

  return (
    <Screen scrollable={true}>
      <Screen.Header
        left={'Back'}
        leftProps={{onPress: back}}
        right={'Config'}
        rightProps={{onPress: openSettings}}>
        <Screen.Title>Current Location</Screen.Title>
        <Screen.Subtitle>Get</Screen.Subtitle>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            The "WHEN-IN-USE" permission is needed to get the user's one-time
            location when the app is in the foreground.
          </Text>

          <Button
            title={'Request Permission'}
            onPress={request}
            style={PageStyle.button}
          />

          <Button
            disabled={!locationAllow || locationLoading}
            title={'Get Current Location'}
            onPress={getCurrentLocation}
            style={PageStyle.button}
          />

          {location && (
            <CardLocation location={location} style={{marginTop: 8}} />
          )}
        </View>
      </Screen.Content>
    </Screen>
  );
};
