import Taro from '@tarojs/taro';

/**
 * 检查是否已登录
 */
export const isLoggedIn = () => {
  return !!Taro.getStorageSync('token');
};

/**
 * 检查是否是管理员
 */
export const isAdmin = () => {
  const userInfo = Taro.getStorageSync('userInfo');
  return userInfo && userInfo.role === 'ADMIN';
};

/**
 * 获取 token
 */
export const getToken = () => {
  return Taro.getStorageSync('token');
};

/**
 * 获取用户信息
 */
export const getUserInfo = () => {
  return Taro.getStorageSync('userInfo');
};

/**
 * 保存登录信息
 */
export const saveLoginInfo = (token, userInfo) => {
  Taro.setStorageSync('token', token);
  Taro.setStorageSync('userInfo', userInfo);
};

/**
 * 登出
 */
export const logout = () => {
  Taro.removeStorageSync('token');
  Taro.removeStorageSync('userInfo');
};
