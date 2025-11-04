import React from 'react';
import {Text, View} from 'react-native';
import {Screen} from '../../commons/Screen';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

export const HomePage = ({children}: PageProps) => {
  return (
    <Screen>
      <Screen.Header>
        <Screen.Title>React Native Location</Screen.Title>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            React Native library for tracking location changes in foreground and
            background.
          </Text>

          {children}
        </View>
      </Screen.Content>
    </Screen>
  );
};
