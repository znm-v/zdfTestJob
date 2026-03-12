import React from 'react';
import { View, Text, Image } from '@tarojs/components';
import { Page } from '../../types';
import './index.css';

const CartPage = ({ cart, cartTotal, onNavigate, onRemoveFromCart, onCheckout }) => {
  return (
    <View className='cart-page'>
      <Text className='cart-title'>购物篮</Text>

      {cart.length === 0 ? (
        <View className='cart-empty'>
          <View className='cart-empty-icon'>
            <Text>🛒</Text>
          </View>
          <Text className='cart-empty-text'>篮子里空空如也...</Text>
          <Text className='cart-empty-link' onClick={() => onNavigate(Page.MENU)}>
            去选购美味 &gt;
          </Text>
        </View>
      ) : (
        <>
          <View className='cart-list'>
            {cart.map((item) => (
              <View key={item.id} className='cart-item'>
                <Image className='cart-item-image' src={item.itemImage} mode='aspectFill' />
                <View className='cart-item-info'>
                  <Text className='cart-item-name'>{item.itemName}</Text>
                  <View className='cart-item-details'>
                    <Text className='cart-item-quantity'>x{item.quantity}</Text>
                    <Text className='cart-item-points'>{item.points} pts</Text>
                  </View>
                </View>
                <View className='cart-item-delete' onClick={() => onRemoveFromCart(item.id)}>
                  <Text>🗑️</Text>
                </View>
              </View>
            ))}
          </View>

          <View className='checkout-panel'>
            <View className='checkout-row'>
              <Text className='checkout-label'>总计积分消耗</Text>
              <Text className='checkout-total'>{cartTotal.toLocaleString()}</Text>
            </View>
            <View className='checkout-btn' onClick={onCheckout}>
              <Text>立即支付 (积分)</Text>
            </View>
          </View>
        </>
      )}
    </View>
  );
};

export default CartPage;
