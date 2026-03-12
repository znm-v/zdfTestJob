import React from 'react';
import { View, Text } from '@tarojs/components';
import './index.css';

const Loader = ({ message }) => (
  <View className='loader-overlay'>
    <View className='loader-spinner'></View>
    <Text className='loader-text'>{message}</Text>
  </View>
);

export default Loader;
