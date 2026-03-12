import Taro from '@tarojs/taro';

const STORAGE_KEYS = {
  POINTS: 'sb_points',
  ORDERS: 'sb_orders'
};

export const storageService = {
  // 获取积分
  getPoints: async () => {
    try {
      const points = await Taro.getStorage({ key: STORAGE_KEYS.POINTS });
      return parseInt(points.data) || 0;
    } catch (error) {
      return 0;
    }
  },

  // 保存积分
  setPoints: async (points) => {
    try {
      await Taro.setStorage({
        key: STORAGE_KEYS.POINTS,
        data: points.toString()
      });
    } catch (error) {
      console.error('Failed to save points:', error);
    }
  },

  // 获取订单列表
  getOrders: async () => {
    try {
      const orders = await Taro.getStorage({ key: STORAGE_KEYS.ORDERS });
      return JSON.parse(orders.data) || [];
    } catch (error) {
      return [];
    }
  },

  // 保存订单列表
  setOrders: async (orders) => {
    try {
      await Taro.setStorage({
        key: STORAGE_KEYS.ORDERS,
        data: JSON.stringify(orders)
      });
    } catch (error) {
      console.error('Failed to save orders:', error);
    }
  }
};
