import Taro from '@tarojs/taro';

// 后端 API 地址
const BASE_URL = 'http://localhost:8080/api/v1';

/**
 * 发送请求
 */
export const request = async (url, options = {}) => {
  const token = Taro.getStorageSync('token');
  
  const config = {
    url: `${BASE_URL}${url}`,
    method: options.method || 'GET',
    header: {
      'Content-Type': 'application/json',
      ...options.header
    },
    ...options
  };

  // 添加 token
  if (token) {
    config.header['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await Taro.request(config);
    
    if (response.statusCode === 200) {
      const data = response.data;
      console.log('响应数据:', data);
      
      // 后端返回的 code 为 200 表示成功
      if (data.code === 200) {
        return data.data;
      } else if (data.code === 401) {
        // Token 过期，清除存储并跳转到登录
        Taro.removeStorageSync('token');
        Taro.removeStorageSync('userInfo');
        Taro.redirectTo({ url: '/pages/login/index' });
        throw new Error(data.message || '未授权');
      } else {
        // 其他错误码（包括 403, 500 等）
        throw new Error(data.message || `请求失败 (code: ${data.code})`);
      }
    } else {
      throw new Error(`请求失败: HTTP ${response.statusCode}`);
    }
  } catch (error) {
    console.error('请求错误:', error);
    throw error;
  }
};

/**
 * GET 请求
 */
export const get = (url, options = {}) => {
  return request(url, { ...options, method: 'GET' });
};

/**
 * POST 请求
 */
export const post = (url, data, options = {}) => {
  return request(url, { ...options, method: 'POST', data });
};

/**
 * PUT 请求
 */
export const put = (url, data, options = {}) => {
  return request(url, { ...options, method: 'PUT', data });
};

/**
 * DELETE 请求
 */
export const del = (url, options = {}) => {
  return request(url, { ...options, method: 'DELETE' });
};
