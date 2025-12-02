import React from 'react';
import {Text, View} from 'react-native';
import {RNLocation} from '@hyoper/rn-location';
import {Screen} from '../../commons/Screen';
import {Button} from '../../commons/Button';
import {openSettings} from '../../utils';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

export const ManagerPage = ({back}: PageProps) => {
  /**
   * Check the status of GPS.
   */
  const checkGps = async () => {
    const name = 'Check Gps';
    RNLocation.manager
      .checkGps()
      .then(status => {
        console.log(`${name} Status`, status);
      })
      .catch(error => {
        console.log(name, error);
      });
  };

  /**
   * This method will only work on Android.
   * There is no direct method to enable GPS on the iOS platform.
   * The IOS platform always rejects.
   */
  const openGps = async () => {
    const name = 'Open Gps';
    RNLocation.manager
      .openGps()
      .then(status => {
        console.log(`${name} Status`, status);
      })
      .catch(error => {
        console.log(name, error);
      });
  };

  /**
   * Using this method, you can direct the user to the GPS activation screen.
   * However, it's necessary to inform the user before redirecting them.
   */
  const redirectGps = async () => {
    const name = 'Redirect Gps';
    RNLocation.manager
      .redirectGps()
      .then(status => {
        console.log(`${name} Success`, status);
      })
      .catch(error => {
        console.log(name, error);
      });
  };

  /**
   * A built-in helper function to inform the user and guide them
   * through the gps settings.
   */
  const redirectGpsAlert = async () => {
    const name = 'Redirect Gps Alert';
    RNLocation.manager
      .redirectGpsAlert()
      .then(status => {
        console.log(`${name} Success`, status);
      })
      .catch(error => {
        console.log(name, error);
      });
  };

  return (
    <Screen scrollable={true}>
      <Screen.Header
        left={'Back'}
        leftProps={{onPress: back}}
        right={'Config'}
        rightProps={{onPress: openSettings}}>
        <Screen.Title>Gps Manager</Screen.Title>
        <Screen.Subtitle>Helpers</Screen.Subtitle>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            Check the status of the GPS, change it, or instruct the user to
            change it.
          </Text>

          <Button
            title={'Check GPS'}
            onPress={checkGps}
            style={PageStyle.button}
          />
          <Button
            title={'Open GPS'}
            onPress={openGps}
            style={PageStyle.button}
          />
          <Button
            title={'Redirect GPS'}
            onPress={redirectGps}
            style={PageStyle.button}
          />
          <Button
            title={'Redirect GPS Alert'}
            onPress={redirectGpsAlert}
            style={PageStyle.button}
          />
        </View>
      </Screen.Content>
    </Screen>
  );
};
