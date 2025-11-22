import React from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  TouchableOpacityProps,
  View,
} from 'react-native';

export type ButtonProps = {
  title: string;
  onPress?: () => void;
} & Omit<TouchableOpacityProps, 'children'>;

export const Button = ({title, style, disabled, ...props}: ButtonProps) => {
  return (
    <TouchableOpacity
      style={StyleSheet.flatten([
        styles.root,
        disabled && styles.disabled,
        style,
      ])}
      disabled={disabled}
      {...props}>
      <View style={styles.container}>
        <Text style={styles.title}>{title}</Text>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  root: {
    height: 44,
    backgroundColor: '#333',
  },
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    color: '#FFF',
  },
  disabled: {
    backgroundColor: '#BABABA',
  },
});
