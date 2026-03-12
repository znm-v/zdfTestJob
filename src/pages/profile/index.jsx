import React from 'react';
import Taro from '@tarojs/taro';
import { View, Text, Image, Button } from '@tarojs/components';
import { Page } from '../../types';
import './index.css';

const ProfilePage = ({ points, userInfo, coupleInfo, onNavigate, onLogout }) => {
  const handleLogout = () => {
    Taro.showModal({
      title: '确认登出',
      content: '确定要登出吗？',
      success: (res) => {
        if (res.confirm) {
          onLogout();
        }
      }
    });
  };

  const handleGenerateInviteCode = async () => {
    try {
      Taro.showLoading({ title: '生成中...' });
      // 这里调用生成邀请码接口
      const code = 'ABC12345'; // 模拟
      Taro.hideLoading();
      
      Taro.showModal({
        title: '邀请码',
        content: `邀请码: ${code}\n\n点击复制分享给伴侣`,
        confirmText: '复制',
        success: (res) => {
          if (res.confirm) {
            Taro.setClipboardData({
              data: code,
              success: () => {
                Taro.showToast({ title: '已复制', icon: 'success' });
              }
            });
          }
        }
      });
    } catch (error) {
      Taro.hideLoading();
      Taro.showToast({ title: '生成失败', icon: 'none' });
    }
  };

  return (
    <View className='profile-page'>
      {/* 用户信息卡片 */}
      <View className='profile-header'>
        <View className='profile-avatar-wrapper'>
          <View className='profile-avatar'>
            <Image 
              src={userInfo?.avatarUrl || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'} 
              mode='aspectFill' 
              className='avatar-image'
            />
          </View>
          <View className='profile-badge'>
            <Text>❤️</Text>
          </View>
        </View>
        <Text className='profile-name'>{coupleInfo?.coupleName || 'Felix & Marry'}</Text>
        <View className='profile-days'>
          <Text className='profile-days-text'>在一起已经</Text>
          <Text className='profile-days-number'>{coupleInfo?.togetherDays || 520}</Text>
          <Text className='profile-days-text'>天</Text>
        </View>
      </View>

      {/* 积分卡片 */}
      <View className='profile-points-card'>
        <View className='points-icon'>🪙</View>
        <View className='points-info'>
          <Text className='points-label'>当前积分</Text>
          <Text className='points-value'>{points?.toLocaleString() || 0}</Text>
        </View>
      </View>

      {/* 菜单列表 */}
      <View className='profile-menu'>
        <View 
          className='profile-menu-item' 
          onClick={() => onNavigate(Page.ORDERS)}
        >
          <View className='profile-menu-left'>
            <View className='profile-menu-icon blue'>
              <Text>📋</Text>
            </View>
            <Text className='profile-menu-text'>点餐历史</Text>
          </View>
          <Text className='profile-menu-arrow'>›</Text>
        </View>

        <View className='profile-menu-item'>
          <View className='profile-menu-left'>
            <View className='profile-menu-icon pink'>
              <Text>❤️</Text>
            </View>
            <Text className='profile-menu-text'>我们的纪念日</Text>
          </View>
          <Text className='profile-menu-arrow'>›</Text>
        </View>

        <View 
          className='profile-menu-item'
          onClick={handleGenerateInviteCode}
        >
          <View className='profile-menu-left'>
            <View className='profile-menu-icon green'>
              <Text>🔗</Text>
            </View>
            <Text className='profile-menu-text'>邀请伴侣</Text>
          </View>
          <Text className='profile-menu-arrow'>›</Text>
        </View>

        {userInfo?.role === 'ADMIN' && (
          <View 
            className='profile-menu-item admin' 
            onClick={() => onNavigate(Page.ADMIN)}
          >
            <View className='profile-menu-left'>
              <View className='profile-menu-icon red'>
                <Text>⚙️</Text>
              </View>
              <Text className='profile-menu-text admin-text'>管理端后台</Text>
            </View>
            <View className='profile-menu-tag'>
              <Text>进入</Text>
            </View>
          </View>
        )}
      </View>

      {/* 爱情寄语 */}
      <View className='profile-quote'>
        <Text className='profile-quote-text'>
          "爱情就像美食，需要用心品味，才能感受到其中的甜蜜。"
        </Text>
      </View>

      {/* 登出按钮 */}
      <View className='profile-footer'>
        <Button 
          className='logout-btn'
          onClick={handleLogout}
        >
          登出
        </Button>
      </View>
    </View>
  );
};

export default ProfilePage;
