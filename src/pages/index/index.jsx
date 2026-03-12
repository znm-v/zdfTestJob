import React, { useState, useEffect } from 'react';
import Taro from '@tarojs/taro';
import { View } from '@tarojs/components';
import { Page } from '../../types';
import { foodAPI, giftAPI, coupleAPI, orderAPI, adminAPI, authAPI, cartAPI } from '../../services/api';
import { isLoggedIn, isAdmin, logout, getUserInfo } from '../../utils/auth';
import { usePoints } from '../../hooks/usePoints';
import Toast from '../../components/Toast';
import Loader from '../../components/Loader';
import NavItem from '../../components/NavItem';
import HomePage from '../home';
import MenuPage from '../menu';
import CartPage from '../cart';
import ProfilePage from '../profile';
import OrdersPage from '../orders';
import DetailPage from '../detail';
import ShopPage from '../shop';
import AdminPage from '../admin';
import './index.css';

export default function Index() {
  const [currentPage, setCurrentPage] = useState(Page.HOME);
  const [menu, setMenu] = useState([]);
  const [shop, setShop] = useState([]);
  const [orders, setOrders] = useState([]);
  const [cart, setCart] = useState([]);
  const [selectedFood, setSelectedFood] = useState(null);
  const [toast, setToast] = useState(null);
  const [loading, setLoading] = useState(null);
  const [userInfo, setUserInfo] = useState(null);
  const [coupleInfo, setCoupleInfo] = useState(null);

  // 使用自定义 Hooks
  const { points, deductPoints, addPoints } = usePoints();

  useEffect(() => {
    // 检查登录状态
    if (!isLoggedIn()) {
      Taro.redirectTo({ url: '/pages/login/index' });
      return;
    }

    // 初始化数据
    initializeData();
  }, []);

  // 初始化数据
  const initializeData = async () => {
    try {
      setLoading('加载中...');
      
      // 获取用户信息
      console.log('1. 获取用户信息');
      const user = getUserInfo();
      console.log('用户信息:', user);
      
      if (!user || !user.coupleAccountId) {
        throw new Error('用户信息不完整');
      }
      
      setUserInfo(user);

      // 获取情侣账户信息（保留，用于显示情侣信息）
      console.log('2. 获取情侣账户信息');
      const couple = await coupleAPI.info();
      console.log('情侣账户信息:', couple);
      setCoupleInfo(couple);

      // 获取食物列表
      console.log('3. 获取食物列表');
      const foodList = await foodAPI.list('', 1, 100);
      console.log('食物列表:', foodList);
      setMenu(foodList.records || foodList || []);

      // 获取礼品列表
      console.log('4. 获取礼品列表');
      const giftList = await giftAPI.list(1, 100);
      console.log('礼品列表:', giftList);
      setShop(giftList.records || giftList || []);

      // 获取购物车列表
      console.log('5. 获取购物车列表');
      const cartList = await loadCart();
      console.log('购物车列表:', cartList);
      setCart(cartList);

      setLoading(null);
      console.log('初始化完成');
    } catch (error) {
      console.error('初始化失败:', error);
      setLoading(null);
      showToast(error.message || '加载失败，请重试');
      
      // 如果是认证错误，跳转到登录页
      if (error.message && error.message.includes('未授权')) {
        setTimeout(() => {
          logout();
          Taro.redirectTo({ url: '/pages/login/index' });
        }, 2000);
      }
    }
  };

  // 加载订单列表
  const loadOrders = async () => {
    try {
      const orderList = await orderAPI.list(null, 1, 100);
      return orderList.records || orderList || [];
    } catch (error) {
      console.error('加载订单失败:', error);
      return [];
    }
  };

  // 加载购物车列表
  const loadCart = async () => {
    try {
      const cartList = await cartAPI.list();
      return cartList || [];
    } catch (error) {
      console.error('加载购物车失败:', error);
      return [];
    }
  };

  // 计算购物车总价
  const calculateCartTotal = (cartItems) => {
    return cartItems.reduce((sum, item) => sum + (item.points * item.quantity), 0);
  };

  const showToast = (msg) => setToast(msg);

  const navigateTo = async (page, data = null) => {
    if (data) {
      setSelectedFood(data);
    }
    
    // 如果导航到订单页面，加载订单数据
    if (page === Page.ORDERS) {
      setLoading('加载订单...');
      const orderList = await loadOrders();
      setOrders(orderList);
      setLoading(null);
    }
    
    // 如果导航到购物车页面，刷新购物车数据
    if (page === Page.CART) {
      setLoading('刷新购物车...');
      const cartList = await loadCart();
      setCart(cartList);
      setLoading(null);
    }
    
    setCurrentPage(page);
    Taro.pageScrollTo({ scrollTop: 0 });
  };

  const handleAddToCart = async (food) => {
    try {
      setLoading('添加中...');
      
      await cartAPI.add({
        itemType: 1,
        itemId: food.id,
        quantity: 1
      });
      
      // 重新加载购物车
      const cartList = await loadCart();
      setCart(cartList);
      
      setLoading(null);
      showToast('已加入购物篮 ❤️');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '添加失败');
    }
  };

  const handleRemoveFromCart = async (cartItemId) => {
    try {
      setLoading('删除中...');
      
      await cartAPI.remove(cartItemId);
      
      // 重新加载购物车
      const cartList = await loadCart();
      setCart(cartList);
      
      setLoading(null);
      showToast('已移除');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '删除失败');
    }
  };

  const handleCheckout = async () => {
    const cartTotal = calculateCartTotal(cart);
    const currentPoints = userInfo.personalPoints || 0;
    
    if (cartTotal > currentPoints) {
      showToast('积分不足，快找另一半投喂吧！');
      return;
    }

    try {
      setLoading('正在下单...');
      
      // 调用后端创建订单接口
      const orderData = {
        orderType: 1,
        items: cart.map(item => ({
          itemType: item.itemType,
          itemId: item.itemId,
          quantity: item.quantity || 1
        })),
        remark: ''
      };

      await orderAPI.create(orderData);
      
      // 清空购物车
      await cartAPI.clear();
      setCart([]);
      
      // 重新获取用户信息以更新积分
      const updatedUser = await authAPI.getUserInfo();
      setUserInfo(updatedUser);
      
      setLoading(null);
      showToast('支付成功！美味即将送达');
      navigateTo(Page.HOME);
    } catch (error) {
      setLoading(null);
      showToast(error.message || '下单失败');
    }
  };

  const handleRedeemGift = async (gift) => {
    const currentPoints = userInfo.personalPoints || 0;
    if (gift.points > currentPoints) {
      showToast('积分不足以兑换此礼品');
      return;
    }

    try {
      setLoading('正在兑换...');
      
      const orderData = {
        orderType: 2,
        items: [{
          itemType: 2,
          itemId: gift.id,
          quantity: 1
        }],
        remark: ''
      };

      await orderAPI.create(orderData);
      
      // 重新获取用户信息以更新积分
      const updatedUser = await authAPI.getUserInfo();
      setUserInfo(updatedUser);
      
      setLoading(null);
      showToast('兑换成功！请查收历史记录');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '兑换失败');
    }
  };

  // 管理员功能
  const handleAddPoints = async (val, targetUserId) => {
    try {
      setLoading('正在发放积分...');
      
      await adminAPI.grantPoints({
        userId: targetUserId || userInfo.id,  // 如果没有指定用户，发放给自己
        points: val,
        remark: '管理员发放'
      });

      // 重新获取用户信息以刷新积分
      const updatedUser = await authAPI.getUserInfo();
      setUserInfo(updatedUser);
      
      setLoading(null);
      showToast(`成功发放 ${val} 积分！`);
    } catch (error) {
      setLoading(null);
      showToast(error.message || '发放失败');
    }
  };

  const handleAddFood = async (food) => {
    try {
      setLoading('正在上架...');
      
      await foodAPI.create(food);
      
      setMenu(prev => [...prev, food]);
      setLoading(null);
      showToast('食物上架成功！');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '上架失败');
    }
  };

  const handleAddGift = async (gift) => {
    try {
      setLoading('正在上架...');
      
      // 构建礼品数据
      const giftData = {
        name: gift.name,
        description: gift.name,
        imageUrl: gift.img,
        points: gift.points,
        stock: 999,
        sales: 0,
        status: 1
      };
      
      const created = await giftAPI.create(giftData);
      
      // 重新获取礼品列表
      const giftList = await giftAPI.list(1, 100);
      setShop(giftList.records || giftList || []);
      
      setLoading(null);
      showToast('礼品上架成功！');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '上架失败');
    }
  };

  const handleRemoveItem = async (type, id) => {
    try {
      setLoading('正在下架...');
      
      if (type === 'food') {
        await foodAPI.delete(id);
        setMenu(prev => prev.filter(i => i.id !== id));
      } else {
        await giftAPI.delete(id);
        setShop(prev => prev.filter(i => i.id !== id));
      }
      
      setLoading(null);
      showToast('已下架');
    } catch (error) {
      setLoading(null);
      showToast(error.message || '下架失败');
    }
  };

  const handleLogout = () => {
    logout();
    Taro.redirectTo({ url: '/pages/login/index' });
  };

  if (!userInfo || !coupleInfo) {
    return <Loader message='加载中...' />;
  }
  
  // 确保 personalPoints 有默认值
  const currentPoints = userInfo.personalPoints || 0;

  return (
    <View className='page-container'>
      {/* 顶部刘海 */}
      {currentPage !== Page.DETAIL && (
        <View className='notch-bar'></View>
      )}

      {/* 页面内容 */}
      {currentPage === Page.HOME && (
        <HomePage
          points={currentPoints}
          menu={menu}
          onNavigate={navigateTo}
        />
      )}

      {currentPage === Page.MENU && (
        <MenuPage
          menu={menu}
          onNavigate={navigateTo}
          onAddToCart={handleAddToCart}
        />
      )}

      {currentPage === Page.CART && (
        <CartPage
          cart={cart}
          cartTotal={calculateCartTotal(cart)}
          onNavigate={navigateTo}
          onRemoveFromCart={handleRemoveFromCart}
          onCheckout={handleCheckout}
        />
      )}

      {currentPage === Page.PROFILE && (
        <ProfilePage
          points={currentPoints}
          userInfo={userInfo}
          coupleInfo={coupleInfo}
          onNavigate={navigateTo}
          onLogout={handleLogout}
        />
      )}

      {currentPage === Page.ORDERS && (
        <OrdersPage
          orders={orders}
          onNavigate={navigateTo}
        />
      )}

      {currentPage === Page.DETAIL && (
        <DetailPage
          food={selectedFood}
          onNavigate={navigateTo}
          onAddToCart={handleAddToCart}
        />
      )}

      {currentPage === Page.SHOP && (
        <ShopPage
          points={currentPoints}
          shop={shop}
          onRedeemGift={handleRedeemGift}
        />
      )}

      {isAdmin() && currentPage === Page.ADMIN && (
        <AdminPage
          points={currentPoints}
          menu={menu}
          shop={shop}
          onNavigate={navigateTo}
          onAddPoints={handleAddPoints}
          onAddFood={handleAddFood}
          onAddGift={handleAddGift}
          onRemoveItem={handleRemoveItem}
        />
      )}

      {/* 底部导航栏 */}
      {currentPage !== Page.SPLASH && currentPage !== Page.DETAIL && currentPage !== Page.ADMIN && currentPage !== Page.ORDERS && currentPage !== Page.SHOP && (
        <View className='bottom-nav'>
          <NavItem
            active={currentPage === Page.HOME}
            icon='🏠'
            label='首页'
            onClick={() => navigateTo(Page.HOME)}
          />
          <NavItem
            active={currentPage === Page.MENU}
            icon='🍴'
            label='点餐'
            onClick={() => navigateTo(Page.MENU)}
          />
          <NavItem
            active={currentPage === Page.CART}
            icon='🛒'
            label='购物车'
            onClick={() => navigateTo(Page.CART)}
            badge={cart.length}
          />
          <NavItem
            active={currentPage === Page.PROFILE}
            icon='👤'
            label='我的'
            onClick={() => navigateTo(Page.PROFILE)}
          />
        </View>
      )}

      {/* Toast & Loader */}
      {toast && <Toast message={toast} onDismiss={() => setToast(null)} />}
      {loading && <Loader message={loading} />}
    </View>
  );
}
