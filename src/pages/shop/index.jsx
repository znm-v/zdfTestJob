import React from 'react';
import { View, Text, Image } from '@tarojs/components';
import './index.css';

const ShopPage = ({ points, shop, onRedeemGift }) => {
  return (
    <View className='shop-page'>
      {/* 页面标题 */}
      <View className='shop-header'>
        <View className='shop-icon'>🎁</View>
        <Text className='shop-title'>礼品兑换</Text>
      </View>

      {/* 积分余额 */}
      <View className='shop-balance'>
        <Text className='shop-balance-label'>可用积分</Text>
        <Text className='shop-balance-value'>{points.toLocaleString()}</Text>
      </View>

      {/* 礼品列表 */}
      <View className='shop-list'>
        {shop.map(item => (
          <View key={item.id} className='shop-item'>
            <Image className='shop-item-image' src={item.imageUrl || item.img} mode='aspectFill' />
            <View className='shop-item-content'>
              <Text className='shop-item-name'>{item.name}</Text>
              <Text className='shop-item-desc'>{item.description || '精选心意好礼，记录美好瞬间'}</Text>
              <View className='shop-item-footer'>
                <View className='shop-item-points-wrapper'>
                  <Text className='shop-item-points-label'>需要消耗</Text>
                  <Text className='shop-item-points'>{item.points.toLocaleString()}</Text>
                </View>
                <View 
                  className={`shop-redeem-btn ${points < item.points ? 'disabled' : ''}`}
                  onClick={() => points >= item.points && onRedeemGift(item)}
                >
                  <Text>{points < item.points ? '积分不足' : '立即兑换'}</Text>
                </View>
              </View>
            </View>
          </View>
        ))}
      </View>

      {/* 兑换说明 */}
      <View className='shop-notice'>
        <Text className='shop-notice-title'>兑换说明</Text>
        <Text className='shop-notice-text'>• 兑换后积分将立即扣除</Text>
        <Text className='shop-notice-text'>• 礼品将在 3-5 个工作日内发货</Text>
        <Text className='shop-notice-text'>• 兑换记录可在"点餐历史"中查看</Text>
      </View>
    </View>
  );
};

export default ShopPage;
