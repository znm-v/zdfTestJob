import React from 'react';
import { View, Text } from '@tarojs/components';
import './index.css';

const NavItem = ({ active, icon, label, onClick, badge }) => (
  <View onClick={onClick} className={`nav-item ${active ? 'active' : ''}`}>
    <View className='nav-icon'>
      <Text>{icon}</Text>
      {badge !== undefined && badge > 0 && (
        <View className='nav-badge'>
          <Text>{badge}</Text>
        </View>
      )}
    </View>
    <Text className='nav-label'>{label}</Text>
  </View>
);

export default NavItem;
