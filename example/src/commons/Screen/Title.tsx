import React from 'react';
import {StyleSheet, Text, TextProps} from 'react-native';

export type TitleProps = TextProps;

export const Title = ({style, children, ...props}: TitleProps) => {
  return (
    <Text style={StyleSheet.flatten([styles.root, style])} {...props}>
      {children}
    </Text>
  );
};

const styles = StyleSheet.create({
  root: {
    color: 'black',
    textTransform: 'capitalize',
    fontSize: 18,
    fontWeight: 700,
  },
});
