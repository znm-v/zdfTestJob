import React from 'react';
import Taro from '@tarojs/taro';
import { View, Text, Image } from '@tarojs/components';
import { Page } from '../../types';
import './index.css';

const HomePage = ({ points, menu, onNavigate }) => {
  return (
    <View className='home-page'>
      <View className='home-header'>
        <View>
          <Text className='home-greeting'>早安, 亲爱的</Text>
          <Text className='home-subtitle'>今天想一起吃点什么?</Text>
        </View>
        <View className='home-avatar'>
          <Image src='https://api.dicebear.com/7.x/avataaars/svg?seed=Felix' mode='aspectFill' />
        </View>
      </View>

      <View className='points-card'>
        <View>
          <Text className='points-label'>当前共享积分</Text>
          <View className='points-value'>
            <Text className='points-icon'>🪙</Text>
            <Text className='points-number'>{(points || 0).toLocaleString()}</Text>
          </View>
        </View>
        <View className='exchange-btn' onClick={() => onNavigate(Page.SHOP)}>
          <Text>去兑换 &gt;</Text>
        </View>
      </View>

      <View className='section-header'>
        <Text className='section-title'>今日推荐</Text>
        <Text className='section-link' onClick={() => onNavigate(Page.MENU)}>查看全部</Text>
      </View>
      
      <View className='recommend-grid'>
        {menu.slice(0, 2).map(item => (
          <View
            key={item.id}
            className='recommend-card'
            onClick={() => onNavigate(Page.DETAIL, item)}
          >
            <Image src={item.img} mode='aspectFill' className='recommend-image' />
            <View className='recommend-card-info'>
              <Text className='recommend-card-name'>{item.name}</Text>
              <Text className='recommend-card-points'>{item.points} 积分</Text>
            </View>
          </View>
        ))}
      </View>

      <View className='love-quote'>
        <Text className='love-quote-title'>爱情寄语</Text>
        <Text className='love-quote-text'>"最好的食物，是和最爱的人一起分享的那一顿。"</Text>
      </View>
    </View>
  );
};

export default HomePage;
