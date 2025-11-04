import React from 'react';
import {View, ViewProps, StyleSheet} from 'react-native';

export type ContentProps = {} & ViewProps;

export const Content = ({style, children}: ContentProps) => {
  return (
    <View style={StyleSheet.flatten([styles.root, style])}>{children}</View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
  },
});
