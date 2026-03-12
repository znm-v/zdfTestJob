import { useState, useEffect } from 'react';
import { storageService } from '../services/storage';

export const useOrders = () => {
  const [orders, setOrders] = useState([]);

  // 加载订单
  useEffect(() => {
    const loadOrders = async () => {
      const savedOrders = await storageService.getOrders();
      if (savedOrders.length > 0) {
        setOrders(savedOrders);
      }
    };
    loadOrders();
  }, []);

  // 保存订单
  useEffect(() => {
    if (orders.length > 0) {
      storageService.setOrders(orders);
    }
  }, [orders]);

  // 创建新订单
  const createOrder = (items, totalCost) => {
    const orderNames = items.map(item => item.name).join(', ').substring(0, 30);
    const newOrder = {
      id: Math.random().toString(36).substr(2, 9),
      date: new Date().toLocaleString(),
      name: orderNames + (items.length > 1 ? '...' : ''),
      cost: totalCost,
      status: 'completed'
    };
    setOrders(prev => [newOrder, ...prev]);
    return newOrder;
  };

  return {
    orders,
    createOrder,
    setOrders
  };
};
