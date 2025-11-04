import React from 'react';
import {View, ViewProps, ScrollView, StyleSheet} from 'react-native';
import {useSafeAreaInsets} from 'react-native-safe-area-context';

export type ScreenProps = {
  scrollable?: boolean;
} & ViewProps;

export const Screen = ({
  scrollable,
  style,
  children,
  ...props
}: ScreenProps) => {
  const insets = useSafeAreaInsets();
  const insetsPadding = {
    paddingTop: insets.top,
    paddingBottom: insets.bottom,
    paddingLeft: insets.left,
    paddingRight: insets.right,
  };

  return (
    <View style={[styles.root, style, insetsPadding]} {...props}>
      {scrollable ? (
        <ScrollView contentContainerStyle={styles.scrollable}>
          {children}
        </ScrollView>
      ) : (
        children
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: '#F4F4F4',
  },
  scrollable: {
    flexGrow: 1,
  },
});
