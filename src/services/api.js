import { get, post, put, del } from '../utils/request';

/**
 * 认证相关 API
 */
export const authAPI = {
  // 用户名密码登录
  login: (username, password) => 
    post('/auth/login', { username, password }),
  
  // 用户注册
  register: (data) => 
    post('/auth/register', data),
  
  // 微信登录
  wechatLogin: (data) => 
    post('/auth/wechat/login', data),
  
  // 使用邀请码加入
  inviteJoin: (data) => 
    post('/auth/invite', data),
  
  // 生成邀请码
  generateInviteCode: () => 
    post('/auth/invite/generate', {}),
  
  // 获取用户信息
  getUserInfo: () => 
    get('/auth/info')
};

/**
 * 食物相关 API
 */
export const foodAPI = {
  // 获取食物列表
  list: (category = '', page = 1, size = 10) => 
    get(`/food/list?category=${category}&page=${page}&size=${size}`),
  
  // 获取食物详情
  detail: (id) => 
    get(`/food/${id}`),
  
  // 创建食物（管理员）
  create: (data) => 
    post('/food/create', data),
  
  // 更新食物（管理员）
  update: (id, data) => 
    put(`/food/update/${id}`, data),
  
  // 删除食物（管理员）
  delete: (id) => 
    del(`/food/${id}`)
};

/**
 * 礼品相关 API
 */
export const giftAPI = {
  // 获取礼品列表
  list: (page = 1, size = 10) => 
    get(`/gift/list?page=${page}&size=${size}`),
  
  // 获取礼品详情
  detail: (id) => 
    get(`/gift/${id}`),
  
  // 创建礼品（管理员）
  create: (data) => 
    post('/gift/create', data),
  
  // 删除礼品（管理员）
  delete: (id) => 
    del(`/gift/${id}`)
};

/**
 * 订单相关 API
 */
export const orderAPI = {
  // 创建订单
  create: (data) => 
    post('/order/create', data),
  
  // 获取订单列表
  list: (status = null, page = 1, size = 10) => {
    let url = `/order/list?page=${page}&size=${size}`;
    if (status) url += `&status=${status}`;
    return get(url);
  },
  
  // 获取订单详情
  detail: (id) => 
    get(`/order/${id}`),
  
  // 取消订单
  cancel: (id) => 
    put(`/order/cancel/${id}`, {})
};

/**
 * 情侣账户相关 API
 */
export const coupleAPI = {
  // 获取情侣账户信息
  info: () => 
    get('/couple/info'),
  
  // 获取积分流水
  pointsLog: (page = 1, size = 10) => 
    get(`/couple/points/log?page=${page}&size=${size}`)
};

/**
 * 管理员相关 API
 */
export const adminAPI = {
  // 发放积分
  grantPoints: (data) => 
    post('/admin/points/grant', data),
  
  // 获取统计数据
  statistics: () => 
    get('/admin/statistics'),
  
  // 获取情侣账户的所有用户
  getUsers: () => 
    get('/admin/users')
};

/**
 * 购物车相关 API
 */
export const cartAPI = {
  // 添加到购物车
  add: (data) => 
    post('/cart/add', data),
  
  // 获取购物车列表
  list: () => 
    get('/cart/list'),
  
  // 删除购物车项
  remove: (id) => 
    del(`/cart/${id}`),
  
  // 清空购物车
  clear: () => 
    del('/cart/clear')
};
