import { useState, useMemo } from 'react';

export const useCart = () => {
  const [cart, setCart] = useState([]);

  // 添加到购物车
  const addToCart = (food) => {
    const newCartItem = {
      ...food,
      cartId: Date.now() + Math.random()
    };
    setCart(prev => [...prev, newCartItem]);
  };

  // 从购物车移除
  const removeFromCart = (cartId) => {
    setCart(prev => prev.filter(item => item.cartId !== cartId));
  };

  // 清空购物车
  const clearCart = () => {
    setCart([]);
  };

  // 计算总价
  const cartTotal = useMemo(() => {
    return cart.reduce((sum, item) => sum + item.points, 0);
  }, [cart]);

  return {
    cart,
    addToCart,
    removeFromCart,
    clearCart,
    cartTotal
  };
};
