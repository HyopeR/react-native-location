import React from 'react';
import {Text, View} from 'react-native';
import {RNLocation} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {Button} from '../../commons/Button';
import {openSettings} from '../../utils';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

export const PermissionPage = ({back}: PageProps) => {
  /**
   * Check the "When-In-Use" permission.
   */
  const checkLocation = async () => {
    const name = 'Check Location';
    RNLocation.permission
      .checkLocation()
      .then(status => console.log(`${name} Status`, status))
      .catch(error => console.log(name, error));
  };

  /**
   * Check the "Always" permission.
   */
  const checkLocationAlways = async () => {
    const name = 'Check Location Always';
    RNLocation.permission
      .checkLocationAlways()
      .then(status => console.log(`${name} Status`, status))
      .catch(error => console.log(name, error));
  };

  /**
   * Request the "When-In-Use" permission.
   */
  const requestLocation = async () => {
    const name = 'Request Location';
    RNLocation.permission
      .requestLocation()
      .then(status => console.log(`${name} Status`, status))
      .catch(error => console.log(name, error));
  };

  /**
   * To get "Always" permission from the user, you must first get "When-In-Use"
   * permission. If you try to get the "Always" permission without getting the "When-In-Use"
   * permission, it will always return blocked.
   */
  const requestLocationAlways = async () => {
    const name = 'Request Location Always';
    RNLocation.permission
      .requestLocationAlways()
      .then(status => console.log(`${name} Status`, status))
      .catch(error => console.log(name, error));
  };

  return (
    <Screen scrollable={true}>
      <Screen.Header
        left={'Back'}
        leftProps={{onPress: back}}
        right={'Config'}
        rightProps={{onPress: openSettings}}>
        <Screen.Title>Permission Manager</Screen.Title>
        <Screen.Subtitle>Helpers</Screen.Subtitle>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            Use helper methods to check or change permission states.
          </Text>

          <Button
            title={'Check Location'}
            onPress={checkLocation}
            style={PageStyle.button}
          />
          <Button
            title={'Check Location Always'}
            onPress={checkLocationAlways}
            style={PageStyle.button}
          />
          <Button
            title={'Request Location'}
            onPress={requestLocation}
            style={PageStyle.button}
          />
          <Button
            title={'Request Location Always'}
            onPress={requestLocationAlways}
            style={PageStyle.button}
          />
        </View>
      </Screen.Content>
    </Screen>
  );
};
