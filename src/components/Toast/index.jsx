import React, { useEffect } from 'react';
import { View, Text } from '@tarojs/components';
import './index.css';

const Toast = ({ message, onDismiss }) => {
  useEffect(() => {
    const timer = setTimeout(onDismiss, 2000);
    return () => clearTimeout(timer);
  }, [onDismiss]);

  return (
    <View className='toast'>
      <Text>{message}</Text>
    </View>
  );
};

export default Toast;
