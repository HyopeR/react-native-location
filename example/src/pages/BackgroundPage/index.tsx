import React from 'react';
import {Text, View} from 'react-native';
import {Screen} from '../../commons/Screen';
import {PageStyle} from '../styles';
import {PageProps} from '../types';

export const BackgroundPage = ({back}: PageProps) => {
  return (
    <Screen>
      <Screen.Header left={'Back'} leftProps={{onPress: back}}>
        <Screen.Title>Background Location Usages</Screen.Title>
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
