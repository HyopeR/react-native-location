import React from 'react';
import {StyleSheet, Text, View, ViewProps} from 'react-native';
import {Location} from '@hyoper/rn-location';

export type CardLocationProps = {
  location: Location;
} & ViewProps;

export const CardLocation = ({
  location,
  style,
  ...props
}: CardLocationProps) => {
  const format = (value?: number) => {
    if (value == null) return undefined;
    return Number.isInteger(value) ? value : value.toFixed(6);
  };

  return (
    <View style={StyleSheet.flatten([styles.root, style])} {...props}>
      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Accuracy＊</Text>
          <Text style={styles.text}>{format(location.accuracy)}</Text>
        </View>
      </View>

      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Latitude＊</Text>
          <Text style={styles.text}>{format(location.latitude)}</Text>
        </View>

        <View style={styles.column}>
          <Text style={styles.title}>Longitude＊</Text>
          <Text style={styles.text}>{format(location.longitude)}</Text>
        </View>
      </View>

      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Altitude＊</Text>
          <Text style={styles.text}>{format(location.altitude)}</Text>
        </View>

        <View style={styles.column}>
          <Text style={styles.title}>Altitude Accuracy＊</Text>
          <Text style={styles.text}>{format(location.altitudeAccuracy)}</Text>
        </View>
      </View>

      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Speed＊</Text>
          <Text style={styles.text}>{format(location.speed)}</Text>
        </View>

        <View style={styles.column}>
          <Text style={styles.title}>Speed Accuracy</Text>
          <Text style={styles.text}>
            {format(location.speedAccuracy) || '-'}
          </Text>
        </View>
      </View>

      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Course＊</Text>
          <Text style={styles.text}>{format(location.course)}</Text>
        </View>

        <View style={styles.column}>
          <Text style={styles.title}>Course Accuracy</Text>
          <Text style={styles.text}>
            {format(location.courseAccuracy) || '-'}
          </Text>
        </View>
      </View>

      <View style={styles.row}>
        <View style={styles.column}>
          <Text style={styles.title}>Timestamp＊</Text>
          <Text style={styles.text}>{location.timestamp}</Text>
          <Text style={styles.text}>
            {new Date(location.timestamp).toISOString()}
          </Text>
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    flexDirection: 'column',
  },
  row: {
    flexDirection: 'row',
    marginBottom: 16,
  },
  column: {
    flex: 1,
    flexDirection: 'column',
    alignItems: 'center',
  },
  title: {
    color: 'gray',
    fontSize: 16,
    fontWeight: 700,
  },
  text: {
    fontSize: 20,
    fontWeight: 500,
  },
});
