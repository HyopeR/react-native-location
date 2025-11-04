import React from 'react';
import {Text, View} from 'react-native';
import {Screen} from '../../commons/Screen';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

export const ForegroundPage = ({back}: PageProps) => {
  return (
    <Screen>
      <Screen.Header left={'Back'} leftProps={{onPress: back}}>
        <Screen.Title>Foreground Location Usages</Screen.Title>
      </Screen.Header>

      <Screen.Content style={PageStyle.root}>
        <View style={PageStyle.body}>
          <Text style={PageStyle.description}>
            To track the user's location when the app is in the foreground, the
            "WHEN-IN-USE" permission is required.
          </Text>
        </View>
      </Screen.Content>
    </Screen>
  );
};
