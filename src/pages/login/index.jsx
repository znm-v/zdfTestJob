import { useState, useEffect } from 'react';
import Taro from '@tarojs/taro';
import { View, Button, Input, Text } from '@tarojs/components';
import { authAPI } from '../../services/api';
import { saveLoginInfo, isLoggedIn } from '../../utils/auth';
import './index.css';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [inviteCode, setInviteCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('login'); // login | invite

  useEffect(() => {
    // 如果已登录，跳转到首页
    if (isLoggedIn()) {
      Taro.redirectTo({ url: '/pages/index/index' });
      return;
    }
    
    // 尝试从本地存储获取上次登录的用户名
    const lastUsername = Taro.getStorageSync('lastUsername');
    if (lastUsername) {
      setUsername(lastUsername);
    }
  }, []);

  // 用户名密码登录
  const handleLogin = async () => {
    if (!username.trim() || !password.trim()) {
      Taro.showToast({ title: '请输入用户名和密码', icon: 'none' });
      return;
    }

    try {
      setLoading(true);
      const result = await authAPI.login(username, password);
      saveLoginInfo(result.token, result.userInfo);
      
      // 保存用户名以便下次登录
      Taro.setStorageSync('lastUsername', username);
      
      Taro.showToast({ title: '登录成功', icon: 'success' });
      Taro.redirectTo({ url: '/pages/index/index' });
    } catch (error) {
      Taro.showToast({ title: error.message || '登录失败', icon: 'none' });
    } finally {
      setLoading(false);
    }
  };

  // 使用邀请码加入
  const handleJoinWithCode = async () => {
    if (!inviteCode.trim()) {
      Taro.showToast({ title: '请输入邀请码', icon: 'none' });
      return;
    }

    try {
      setLoading(true);
      
      // 清除旧的登录信息
      Taro.removeStorageSync('token');
      Taro.removeStorageSync('userInfo');
      
      const result = await authAPI.inviteJoin({
        inviteCode,
        nickName: '新用户',
        avatarUrl: ''
      });
      
      saveLoginInfo(result.token, result.userInfo);
      
      // 保存用户名
      Taro.setStorageSync('lastUsername', result.userInfo.username);
      
      // 显示用户名提示
      Taro.showModal({
        title: '加入成功',
        content: `您的用户名是: ${result.userInfo.username}\n默认密码: 123456\n请妥善保管`,
        showCancel: false,
        success: () => {
          Taro.redirectTo({ url: '/pages/index/index' });
        }
      });
    } catch (error) {
      Taro.showToast({ title: error.message || '加入失败', icon: 'none' });
    } finally {
      setLoading(false);
    }
  };

  // 微信登录
  const handleWechatLogin = async () => {
    try {
      setLoading(true);
      
      // 获取登录 code
      const loginRes = await Taro.login();
      const code = loginRes.code;
      
      // 调用后端微信登录接口，后端负责调用微信 API 获取用户信息
      const result = await authAPI.wechatLogin({ code });
      
      saveLoginInfo(result.token, result.userInfo);
      Taro.showToast({ title: '登录成功', icon: 'success' });
      Taro.redirectTo({ url: '/pages/index/index' });
    } catch (error) {
      Taro.showToast({ title: error.message || '微信登录失败', icon: 'none' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <View className='login-container'>
      <View className='login-header'>
        <Text className='login-title'>💕 SweetBite</Text>
        <Text className='login-subtitle'>情侣点餐小程序</Text>
      </View>

      {/* 标签页 */}
      <View className='tab-container'>
        <View 
          className={`tab-item ${activeTab === 'login' ? 'active' : ''}`}
          onClick={() => setActiveTab('login')}
        >
          账号登录
        </View>
        <View 
          className={`tab-item ${activeTab === 'invite' ? 'active' : ''}`}
          onClick={() => setActiveTab('invite')}
        >
          邀请码加入
        </View>
      </View>

      {/* 账号登录 */}
      {activeTab === 'login' && (
        <View className='login-form'>
          <Input
            className='input-field'
            placeholder='用户名'
            value={username}
            onInput={(e) => setUsername(e.detail.value)}
            placeholderStyle='color: #999'
          />
          <Input
            className='input-field'
            placeholder='密码'
            password
            value={password}
            onInput={(e) => setPassword(e.detail.value)}
            placeholderStyle='color: #999'
          />
          <Button 
            className='login-btn'
            onClick={handleLogin}
            loading={loading}
          >
            登录
          </Button>
        </View>
      )}

      {/* 邀请码加入 */}
      {activeTab === 'invite' && (
        <View className='login-form'>
          <Input
            className='input-field'
            placeholder='输入邀请码'
            value={inviteCode}
            onInput={(e) => setInviteCode(e.detail.value)}
            placeholderStyle='color: #999'
          />
          <Button 
            className='login-btn'
            onClick={handleJoinWithCode}
            loading={loading}
          >
            使用邀请码加入
          </Button>
        </View>
      )}

      {/* 微信登录 */}
      <View className='divider'>或</View>
      <Button 
        className='wechat-btn'
        onClick={handleWechatLogin}
        loading={loading}
      >
        🔐 微信登录
      </Button>

      <View className='login-footer'>
        <Text className='footer-text'>首次登录将自动注册账户</Text>
      </View>
    </View>
  );
}
