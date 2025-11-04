import React from 'react';
import {
  StyleSheet,
  Text,
  TextProps,
  TouchableOpacity,
  TouchableOpacityProps,
  View,
} from 'react-native';

export type CardProps = {
  title: string;
  titleProps?: TextProps;
} & TouchableOpacityProps;

export const Card = ({title, titleProps, style, ...props}: CardProps) => {
  const {style: titleStyle, ...omitTitleProps} = titleProps || {};
  return (
    <TouchableOpacity style={styles.root} {...props}>
      <View style={StyleSheet.flatten([styles.container, style])}>
        <Text
          style={StyleSheet.flatten([styles.title, titleStyle])}
          {...omitTitleProps}>
          {title}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  root: {},
  container: {
    // flex: 1,
    paddingVertical: 8,
    backgroundColor: '#FAFAFA',
    borderColor: '#FAFAFA',
    borderWidth: 2,
  },
  title: {
    textTransform: 'capitalize',
    fontSize: 14,
  },
});
