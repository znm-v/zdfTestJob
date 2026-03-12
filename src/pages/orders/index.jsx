import React from 'react';
import { View, Text } from '@tarojs/components';
import { Page } from '../../types';
import './index.css';

const OrdersPage = ({ orders, onNavigate }) => {
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getOrderTypeName = (type) => {
    return type === 1 ? '食物订单' : '礼品兑换';
  };

  const getStatusName = (status) => {
    const statusMap = {
      0: '待支付',
      1: '已完成',
      2: '已取消'
    };
    return statusMap[status] || '未知';
  };

  return (
    <View className='orders-page'>
      {/* 返回按钮 */}
      <View className='orders-header' onClick={() => onNavigate(Page.PROFILE)}>
        <Text className='orders-back'>←</Text>
        <Text className='orders-header-text'>历史足迹</Text>
      </View>

      {/* 订单列表 */}
      {orders.length === 0 ? (
        <View className='orders-empty'>
          <Text className='orders-empty-icon'>📋</Text>
          <Text className='orders-empty-text'>尚无点餐记录</Text>
        </View>
      ) : (
        <View className='orders-list'>
          {orders.map(order => (
            <View key={order.id} className='order-card'>
              <View className='order-header'>
                <Text className='order-type'>{getOrderTypeName(order.orderType)}</Text>
                <Text className='order-status'>{getStatusName(order.status)}</Text>
              </View>
              <Text className='order-date'>{formatDate(order.createTime)}</Text>
              
              {/* 订单项 */}
              {order.items && order.items.length > 0 && (
                <View className='order-items'>
                  {order.items.map((item, index) => (
                    <Text key={index} className='order-item-name'>
                      {item.itemName} x{item.quantity}
                    </Text>
                  ))}
                </View>
              )}
              
              <View className='order-footer'>
                <Text className='order-id'>订单号: {order.orderNo}</Text>
                <Text className='order-cost'>-{order.totalPoints.toLocaleString()} pts</Text>
              </View>
            </View>
          ))}
        </View>
      )}
    </View>
  );
};

export default OrdersPage;
