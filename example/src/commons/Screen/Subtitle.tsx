import React from 'react';
import {StyleSheet, Text, TextProps} from 'react-native';

export type SubtitleProps = TextProps;

export const Subtitle = ({style, children, ...props}: SubtitleProps) => {
  return (
    <Text style={StyleSheet.flatten([styles.root, style])} {...props}>
      {children}
    </Text>
  );
};

const styles = StyleSheet.create({
  root: {
    color: 'gray',
    // textTransform: 'capitalize',
    fontSize: 15,
    fontWeight: 500,
  },
});
