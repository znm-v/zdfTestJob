import React, { useState } from 'react';
import { View, Text, Image } from '@tarojs/components';
import { Page, FLAVORS } from '../../constants';
import './index.css';

const DetailPage = ({ food, onNavigate, onAddToCart }) => {
  const [selectedFlavor, setSelectedFlavor] = useState(FLAVORS[1]);

  if (!food) {
    return null;
  }

  const handleAddToCart = () => {
    onAddToCart(food);
    onNavigate(Page.CART);
  };

  return (
    <View className='detail-page'>
      {/* 商品图片 */}
      <View className='detail-image-wrapper'>
        <Image className='detail-image' src={food.img} mode='aspectFill' />
        <View className='detail-image-overlay'></View>
        <View className='detail-back-btn' onClick={() => onNavigate(Page.MENU)}>
          <Text className='detail-back-icon'>‹</Text>
        </View>
      </View>

      {/* 商品信息 */}
      <View className='detail-content'>
        <View className='detail-header'>
          <View className='detail-header-left'>
            <Text className='detail-name'>{food.name}</Text>
            <Text className='detail-category'>{food.category}</Text>
          </View>
          <View className='detail-header-right'>
            <Text className='detail-points'>{food.points}</Text>
            <Text className='detail-points-label'>积分消耗</Text>
          </View>
        </View>

        <Text className='detail-desc'>{food.desc}</Text>

        {/* 口味选择 */}
        <View className='detail-flavor-section'>
          <Text className='detail-flavor-title'>口味选择</Text>
          <View className='detail-flavor-options'>
            {FLAVORS.map(flavor => (
              <View
                key={flavor}
                className={`detail-flavor-btn ${selectedFlavor === flavor ? 'active' : 'inactive'}`}
                onClick={() => setSelectedFlavor(flavor)}
              >
                <Text>{flavor}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* 营养信息 */}
        <View className='detail-nutrition'>
          <Text className='detail-nutrition-title'>营养信息</Text>
          <View className='detail-nutrition-grid'>
            <View className='detail-nutrition-item'>
              <Text className='detail-nutrition-value'>520</Text>
              <Text className='detail-nutrition-label'>卡路里</Text>
            </View>
            <View className='detail-nutrition-item'>
              <Text className='detail-nutrition-value'>25g</Text>
              <Text className='detail-nutrition-label'>蛋白质</Text>
            </View>
            <View className='detail-nutrition-item'>
              <Text className='detail-nutrition-value'>15g</Text>
              <Text className='detail-nutrition-label'>脂肪</Text>
            </View>
            <View className='detail-nutrition-item'>
              <Text className='detail-nutrition-value'>45g</Text>
              <Text className='detail-nutrition-label'>碳水</Text>
            </View>
          </View>
        </View>
      </View>

      {/* 底部按钮 */}
      <View className='detail-bottom-bar'>
        <View className='detail-add-cart-btn' onClick={handleAddToCart}>
          <Text className='detail-add-cart-text'>加入购物车</Text>
          <Text className='detail-add-cart-points'>{food.points} pts</Text>
        </View>
      </View>
    </View>
  );
};

export default DetailPage;
